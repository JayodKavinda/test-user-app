package com.example.userapp.controller;

import com.example.userapp.exception.ResourceNotFoundException;
import com.example.userapp.payload.UserDto;
import com.example.userapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreateUser_thenReturnSavedUser() throws Exception{

        // given - precondition or setup
        UserDto userDto = UserDto.builder()
                .firstName("Jayod")
                .lastName("Jayasekara")
                .email("jayod@gmail.com")
                .build();
        given(userService.saveUser(any(UserDto.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(userDto.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(userDto.getEmail())));

    }

    // JUnit test for Get All employees REST API
    @Test
    public void whenGetAllUsers_thenReturnUsersList() throws Exception{
        // given - precondition or setup
        List<UserDto> listOfUsers = new ArrayList<>();
        listOfUsers.add(UserDto.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
        listOfUsers.add(UserDto.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(userService.getAllUsers()).willReturn(listOfUsers);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/users"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));

    }


    @Test
    public void whenGetUserById_thenReturnUserObject() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        UserDto userDto = UserDto.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(userDto);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/users/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

    }


    @Test
    public void whenGetUserByInvalidId_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long userId = 100L;

        given(userService.getUserById(userId)).willThrow(new ResourceNotFoundException("user", userId));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", userId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        UserDto savedUser = UserDto.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        UserDto updatedUser = UserDto.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(savedUser);
        given(userService.updateUser(any(UserDto.class),eq(userId)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedUser.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }


    @Test
    public void whenDeleteUser_thenReturnOk() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        willDoNothing().given(userService).deleteUserById(userId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/users/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}