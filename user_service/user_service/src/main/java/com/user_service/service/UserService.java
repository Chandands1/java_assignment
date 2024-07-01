package com.user_service.service;

import com.user_service.entity.Role;
import com.user_service.entity.User;
import com.user_service.repository.RoleRepository;
import com.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "user-events";

    @Transactional
    public User registerUser(User user) {
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                roles.add(roleRepository.findByName(role.getName()));
            }
        }
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        kafkaTemplate.send(TOPIC, "User registered: " + savedUser.getUsername());
        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        Set<Role> roles = new HashSet<>();
        if (userDetails.getRoles() != null) {
            for (Role role : userDetails.getRoles()) {
                roles.add(roleRepository.findByName(role.getName()));
            }
        }
        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        kafkaTemplate.send(TOPIC, "User updated: " + updatedUser.getUsername());
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.deleteById(id);
        kafkaTemplate.send(TOPIC, "User deleted: " + user.getUsername());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}