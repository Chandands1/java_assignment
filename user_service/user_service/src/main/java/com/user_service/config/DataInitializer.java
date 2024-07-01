package com.user_service.config;

import com.user_service.entity.Role;
import com.user_service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_USER") == null) {
                roleRepository.save(new Role(null, "ROLE_USER", null));
            }
            if (roleRepository.findByName("ROLE_ADMIN") == null) {
                roleRepository.save(new Role(null, "ROLE_ADMIN", null));
            }
        };
    }
}