package com.springboot.taskapi.config;

import com.springboot.taskapi.enums.Role;
import com.springboot.taskapi.model.User;
import com.springboot.taskapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Step 1: prepare the sample usernames
        List<String> usernames = List.of("employee1", "employee2", "employee3");

        // Step 2: create each employee with username as password
        for (String username : usernames) {
            if (!userRepository.existsByUsername(username)) {
                User user = new User(username, passwordEncoder.encode(username), Role.EMPLOYEE);
                userRepository.save(user);
                // Step 3: log the created username
                log.info("User created with username: {}", username);
            }
        }
    }
}
