package com.user_service.test;

import com.fasterxml.jackson.databind.introspect.AnnotatedAndMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.user_service.entity.Role;
import com.user_service.entity.User;
import com.user_service.repository.RoleRepository;
import com.user_service.repository.UserRepository;
import com.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        // Create a mock user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // Create a mock role
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        // Mocking roleRepository to return the role when findByName is called
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        // Set roles for the user
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles((org.hibernate.mapping.Set) roles);

        // Mocking userRepository to return saved user
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Test registerUser method
        User savedUser = userService.registerUser(user);

        // Assertions
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertEquals(1, savedUser.getRoles().isIdentified()); // Assuming one role is added
    }

    @Test
    public void testUpdateUser() {
        // Create a mock user
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existinguser@example.com");

        // Mocking userRepository to return existing user
        AnnotationMap Optional;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Updated details for the user
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@example.com");

        // Test updateUser method
        User savedUser = userService.updateUser(userId, updatedUser);

        // Assertions
        assertNotNull(savedUser);
        assertEquals("updateduser", savedUser.getUsername());
        assertEquals("updateduser@example.com", savedUser.getEmail());
    }

    // Additional unit tests for deleteUser, getUserById, getAllUsers, etc.
}
