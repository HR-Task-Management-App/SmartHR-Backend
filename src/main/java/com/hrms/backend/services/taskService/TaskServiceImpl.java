package com.hrms.backend.services.taskService;

import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.entities.Company;
import com.hrms.backend.entities.Task;
import com.hrms.backend.entities.User;
import com.hrms.backend.entities.enums.Role;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        task.setAssignee(hrId); // HR is assigning the task
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        //allowing only user that are present in company and database
        List<User> users = userRepository.findAllByCompanyCodeAndIdIn(company.getCompanyCode(),taskRequestDto.getEmployees());
        Set<String>  realEmp = users.stream().map(User::getId).collect(Collectors.toSet());
        task.setEmployees(realEmp);
        // 3. Upload image if present
        if (image != null && !image.isEmpty()) {
            String imageUrl = superbaseImageStorageServiceInterface.uploadImage(image, BUCKET_NAME);
            task.setImageUrl(imageUrl);
        }

        Task savedTask = taskRepository.save(task);

        return mapper.map(savedTask, TaskResponseDto.class);
    }

    @Override
    public TaskResponseDto getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadApiRequestException("Task not found"));
        return mapper.map(task, TaskResponseDto.class);
    }

    @Override
    public List<TaskResponseDto> getCompanyTasks(String hrId) {
        User user = userRepository.findById(hrId)
                .orElseThrow(() -> new BadApiRequestException("User not found!!"));
        List<Task> tasks = taskRepository.findAllByCompanyCode(user.getCompanyCode());
        return tasks.stream().map(task -> mapper.map(task, TaskResponseDto.class)).toList();
    }

    @Override
    public List<TaskResponseDto> getEmployeeTasks(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadApiRequestException("User not found!!"));
        List<Task> tasks = taskRepository.findAllByCompanyCodeAndEmployeesContaining(user.getCompanyCode(),userId);
        return tasks.stream().map(task -> mapper.map(task, TaskResponseDto.class)).toList();
    }

    @Override
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto, String taskId) { //hr
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadApiRequestException("Task not found"));
        mapper.map(taskRequestDto, task);
        task.setUpdatedAt(LocalDateTime.now());
        List<User> users = userRepository.findAllByCompanyCodeAndIdIn(task.getCompanyCode(),taskRequestDto.getEmployees());
        Set<String>  realEmp = users.stream().map(User::getId).collect(Collectors.toSet());
        task.setEmployees(realEmp);
        Task save = taskRepository.save(task);
        return mapper.map(save, TaskResponseDto.class);
    }

    @Override
    public SuccessApiResponseMessage deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
        return new SuccessApiResponseMessage("Successfully deleted the task");
    }

    @Override
    public TaskResponseDto updateTaskStatus(TaskRequestDto taskRequestDto,String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadApiRequestException("Task not found, try again!!"));
        task.setStatus(Status.valueOf(taskRequestDto.getStatus()));
        task.setUpdatedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        return mapper.map(saved,TaskResponseDto.class);
    }
}
