package com.hrms.backend;

import com.hrms.backend.dtos.entityDtos.Role;
import com.hrms.backend.entities.User;
import com.hrms.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HrmsApplication implements CommandLineRunner {

	public static void main(String[] args)  {
		SpringApplication.run(HrmsApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		boolean adminUser = userRepository.findByEmail("vikashwork321@gmail.com").isPresent();
		if(!adminUser){
			User user = new User();
			user.setName("Vikash");
			user.setEmail("vikashwork321@gmail.com"); //username is email here
			user.setGender("M");
			user.setPassword(passwordEncoder.encode("Vikash"));
			user.setRole(Role.ROLE_ADMIN);
			userRepository.save(user);
		}
	}
}
