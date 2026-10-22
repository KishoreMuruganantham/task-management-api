package com.springboot.taskapi.service;

import com.springboot.taskapi.enums.Role;
import com.springboot.taskapi.model.User;
import com.springboot.taskapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {
    @InjectMocks
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsernameTest(){
        User user = new User("employee1", "pwd", Role.EMPLOYEE);

        when(userRepository.loadUserByUsername("employee1", true)).thenReturn(user);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("employee1");

        assertEquals("employee1", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("EMPLOYEE", userDetails.getAuthorities().iterator().next().getAuthority());

        verify(userRepository, times(1)).loadUserByUsername("employee1", true);
    }

    @Test
    public void loadUserByUsernameTest_Invalid(){
        when(userRepository.loadUserByUsername("employee5", true)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername("employee5"));

        verify(userRepository, times(1)).loadUserByUsername("employee5", true);
    }
}
