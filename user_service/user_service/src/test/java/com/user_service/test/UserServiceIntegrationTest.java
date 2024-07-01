package com.user_service.test;

import com.user_service.entity.Role;
import com.user_service.entity.User;
import com.user_service.repository.RoleRepository;
import com.user_service.repository.UserRepository;
import com.user_service.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testRegisterUser() {
        // Create a mock user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // Create a mock role
        Role role = new Role();
        role.setName("ROLE_USER");

        // Save role to database
        roleRepository.save(role);

        // Set roles for the user
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Test registerUser method
        User savedUser = userService.registerUser(user);

        // Assertions
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertEquals(1, savedUser.getRoles().size()); // Assuming one role is added
    }

    @Test
    public void testUpdateUser() {
        // Create a mock user and save to database
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        // Updated details for the user
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@example.com");

        // Test updateUser method
        User savedUser = userService.updateUser(user.getId(), updatedUser);

        // Assertions
        assertEquals(user.getId(), savedUser.getId());
        assertEquals("updateduser", savedUser.getUsername());
        assertEquals("updateduser@example.com", savedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        // Create a mock user and save to database
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        // Test deleteUser method
        userService.deleteUser(user.getId());

        // Assertions
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(!deletedUser.isPresent());
    }

    @Test
    public void testGetUserById() {
        // Create a mock user and save to database
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        // Test getUserById method
        Optional<User> foundUser = userService.getUserById(user.getId());

        // Assertions
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("testuser@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testGetAllUsers() {
        // Create mock users and save to database
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        userRepository.save(user2);

        // Test getAllUsers method
        List<User> users = userService.getAllUsers();

        // Assertions
        assertNotNull(users);
        assertEquals(2, users.size()); // Assuming two users were saved
    }
}
