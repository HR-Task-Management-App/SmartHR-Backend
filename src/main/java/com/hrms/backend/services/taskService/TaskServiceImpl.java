package com.hrms.backend.services.taskService;

import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.entities.Company;
import com.hrms.backend.entities.Task;
import com.hrms.backend.entities.User;
import com.hrms.backend.entities.enums.Status;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.TaskRepository;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.services.superbaseImageStorageService.SuperbaseImageStorageServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskServiceInterface{

    @Autowired
    private SuperbaseImageStorageServiceInterface superbaseImageStorageServiceInterface;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper mapper;

    private static final String BUCKET_NAME = "task-images";

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, MultipartFile image, String hrId) {
        // 1. Validate HR and Company
        Company company = companyRepository.findByHr(hrId)
                .orElseThrow(() -> new BadApiRequestException("HR does not belong to any company"));

        User hr = userRepository.findById(hrId)
                .orElseThrow(() -> new BadApiRequestException("HR user not found"));

        // 2. Map task DTO to entity and populate fields
        Task task = mapper.map(taskRequestDto, Task.class);
        task.setCompanyCode(company.getCompanyCode());
        task.setStatus(Status.NOT_STARTED);
        task.setAssignee(hrId); // HR is assigning the task

        // 3. Upload image if present
        if (image != null && !image.isEmpty()) {
            String imageUrl = superbaseImageStorageServiceInterface.uploadImage(image, BUCKET_NAME);
            task.setImageUrl(imageUrl);
        }

        // 4. Save task first
        Task savedTask = taskRepository.save(task);

        // 5. Assign task to HR
        hr.getTasks().add(savedTask.getId());
        userRepository.save(hr);

        // 6. Assign task to selected employees
        List<User> employees = userRepository.findAllByIdIn(taskRequestDto.getEmployees());
        for (User emp : employees) {
            emp.getTasks().add(savedTask.getId());
        }
        userRepository.saveAll(employees);

        // 7. Prepare response
        List<UserInfo> employeeInfos = employees.stream()
                .map(emp -> UserInfo.builder()
                        .id(emp.getId())
                        .name(emp.getName())
                        .email(emp.getEmail())
                        .imageUrl(emp.getImageUrl())
                        .build())
                .toList();

        TaskResponseDto response = mapper.map(savedTask, TaskResponseDto.class);
        response.setAssignee(UserInfo.builder()
                .id(hr.getId())
                .email(hr.getEmail())
                .name(hr.getName())
                .imageUrl(hr.getImageUrl())
                .build());
        response.setEmployees(employeeInfos);

        return response;
    }

}
