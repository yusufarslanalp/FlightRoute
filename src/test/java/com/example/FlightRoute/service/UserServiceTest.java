package com.example.FlightRoute.service;

import com.example.FlightRoute.model.User;
import com.example.FlightRoute.repository.UserRepository;
import com.example.FlightRoute.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testuser1");
        testUser1.setPasswordHash("encodedPassword1");
        testUser1.setRole(UserRole.ADMIN);

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testuser2");
        testUser2.setPasswordHash("encodedPassword2");
        testUser2.setRole(UserRole.AGENCY);
    }

    @Test
    void findByUsername_ExistingUsername_ReturnsUser() {
        String username = "testuser1";
        Optional<User> expectedUser = Optional.of(testUser1);
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(testUser1, result.get());
        assertEquals("testuser1", result.get().getUsername());
        assertEquals(UserRole.ADMIN, result.get().getRole());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_NonExistingUsername_ReturnsEmptyOptional() {
        String username = "nonexistent";
        Optional<User> emptyOptional = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(emptyOptional);

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_NullUsername_ReturnsEmptyOptional() {
        String username = null;
        Optional<User> emptyOptional = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(emptyOptional);

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_EmptyUsername_ReturnsEmptyOptional() {
        String username = "";
        Optional<User> emptyOptional = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(emptyOptional);

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_CaseInsensitiveSearch_HandlesCorrectly() {
        String username = "TestUser1";
        Optional<User> expectedUser = Optional.of(testUser1);
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(testUser1, result.get());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_RepositoryThrowsException_PropagatesException() {
        String username = "testuser1";
        when(userRepository.findByUsername(username))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userService.findByUsername(username));
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_WithDifferentUserRoles_ReturnsCorrectUser() {
        String adminUsername = "testuser1";
        String agencyUsername = "testuser2";

        when(userRepository.findByUsername(adminUsername)).thenReturn(Optional.of(testUser1));
        when(userRepository.findByUsername(agencyUsername)).thenReturn(Optional.of(testUser2));

        Optional<User> adminResult = userService.findByUsername(adminUsername);
        Optional<User> agencyResult = userService.findByUsername(agencyUsername);

        assertTrue(adminResult.isPresent());
        assertEquals(UserRole.ADMIN, adminResult.get().getRole());

        assertTrue(agencyResult.isPresent());
        assertEquals(UserRole.AGENCY, agencyResult.get().getRole());

        verify(userRepository).findByUsername(adminUsername);
        verify(userRepository).findByUsername(agencyUsername);
    }

    @Test
    void findByUsername_WithSpecialCharacters_HandlesCorrectly() {
        String username = "user@domain.com";
        User specialUser = new User();
        specialUser.setId(3L);
        specialUser.setUsername(username);
        specialUser.setPasswordHash("encodedPassword3");
        specialUser.setRole(UserRole.AGENCY);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(specialUser));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(specialUser, result.get());
        assertEquals("user@domain.com", result.get().getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_WithLongUsername_HandlesCorrectly() {
        String longUsername = "verylongusernamethatexceedsnormallengths";
        User longUsernameUser = new User();
        longUsernameUser.setId(4L);
        longUsernameUser.setUsername(longUsername);
        longUsernameUser.setPasswordHash("encodedPassword4");
        longUsernameUser.setRole(UserRole.AGENCY);

        when(userRepository.findByUsername(longUsername)).thenReturn(Optional.of(longUsernameUser));

        Optional<User> result = userService.findByUsername(longUsername);

        assertTrue(result.isPresent());
        assertEquals(longUsernameUser, result.get());
        assertEquals(longUsername, result.get().getUsername());
        verify(userRepository).findByUsername(longUsername);
    }

    @Test
    void findByUsername_VerifyPasswordEncoderNotUsed() {
        String username = "testuser1";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser1));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        verify(passwordEncoder, never()).encode(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository).findByUsername(username);
    }
}
