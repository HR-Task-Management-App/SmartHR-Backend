package com.hrms.backend.controllers;

import com.hrms.backend.entities.Company;
import com.hrms.backend.entities.User;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.EmailConfirmationTokenRepository;
import com.hrms.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/invite")
public class InviteController {

    @Autowired
    private EmailConfirmationTokenRepository emailTokenRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmployee(@RequestParam String token) {
        var t = emailTokenRepo.findByToken(token);
        if (t == null) return ResponseEntity.badRequest().body("Invalid token");

        User emp = t.getUser();
        String companyCode = t.getCompanyCode();
        Company comp = companyRepo.findByCompanyCode(companyCode)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        emp.setCompanyCode(companyCode);
        userRepository.save(emp);

        var emps = comp.getEmployees();
        if (emps == null) emps = new ArrayList<>();
        emps.add(emp.getUserId());
        comp.setEmployees(emps);
        companyRepo.save(comp);

        emailTokenRepo.delete(t);
        return ResponseEntity.ok("Employee approved");
    }
}
