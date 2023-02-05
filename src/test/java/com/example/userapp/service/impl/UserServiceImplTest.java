package com.example.userapp.service.impl;

import com.example.userapp.entity.User;
import com.example.userapp.exception.ResourceNotFoundException;
import com.example.userapp.payload.UserDto;
import com.example.userapp.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void when_SaveUserDto_then_ReturnSameUser() {
        UserDto userDto = UserDto.builder()
                .firstName("firstNameNew")
                .lastName("lastNameNew")
                .email("emailNew@gmail.com")
                .build();

        User user = User.builder()
                .firstName("firstNameNew")
                .lastName("lastNameNew")
                .email("emailNew@gmail.com")
                .build();
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        UserDto returnUser = userService.saveUser(userDto);
        Assertions.assertThat(returnUser.getFirstName()).isEqualTo("firstNameNew");
        Assertions.assertThat(returnUser.getLastName()).isEqualTo("lastNameNew");
        Assertions.assertThat(returnUser.getEmail()).isEqualTo("emailNew@gmail.com");
    }

    @Test
    public void when_updateUserWithId_then_ReturnUpdatedUserObject() {
        long userId = 1L;
        UserDto newUserDto = UserDto.builder()
                .firstName("changedName")
                .lastName("changedName")
                .email("emailNew@gmail.com")
                .build();

        User user = User.builder()
                .firstName("OrgName")
                .lastName("OrgName")
                .email("emailNew@gmail.com")
                .build();

        User updatedUser = User.builder()
                .firstName("changedName")
                .lastName("changedName")
                .email("emailNew@gmail.com")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        when(modelMapper.map(updatedUser, UserDto.class)).thenReturn(newUserDto);
        when(modelMapper.map(newUserDto, User.class)).thenReturn(updatedUser);

        UserDto returnUser = userService.updateUser(newUserDto, userId);
        verify(userRepository).save(user);

        Assertions.assertThat(returnUser.getFirstName()).isEqualTo("changedName");
        Assertions.assertThat(returnUser.getLastName()).isEqualTo("changedName");
        Assertions.assertThat(returnUser.getEmail()).isEqualTo("emailNew@gmail.com");
    }

    @Test
    public void whenGetByInvalidId_then_ThrowResourceNotFoundException() {
        long userId = 1000L;
        when(userRepository.getById(userId)).thenThrow(new RuntimeException());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).
                isThrownBy(() -> userService.getUserById(userId)).
                withMessage("user not found for ID: 1000");
    }

    @Test
    public void when_getUserById_then_ReturnUserObject() {
        UserDto userDto = UserDto.builder()
                .firstName("firstNameNew")
                .lastName("lastNameNew")
                .email("emailNew@gmail.com")
                .build();

        User user = User.builder()
                .firstName("firstNameNew")
                .lastName("lastNameNew")
                .email("emailNew@gmail.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        UserDto returnUser = userService.getUserById(1L);
        Assertions.assertThat(returnUser.getFirstName()).isEqualTo("firstNameNew");
        Assertions.assertThat(returnUser.getLastName()).isEqualTo("lastNameNew");
        Assertions.assertThat(returnUser.getEmail()).isEqualTo("emailNew@gmail.com");

    }

    @Test
    public void when_getUsers_then_ReturnUserObject() {

        List<UserDto> listOfUsers = new ArrayList<>();
        UserDto userDto1 = UserDto.builder().firstName("Ramesh").lastName("Silva").email("ramesh@gmail.com").build();
        UserDto userDto2 = UserDto.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build();
        listOfUsers.add(userDto1);
        listOfUsers.add(userDto2);

        List<User> users = new ArrayList<>();
        User user1 = User.builder().firstName("Ramesh").lastName("Silva").email("ramesh@gmail.com").build();
        User user2 = User.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        when(modelMapper.map(user1, UserDto.class)).thenReturn(userDto1);
        when(modelMapper.map(user2, UserDto.class)).thenReturn(userDto2);
        List<UserDto> returnUsers = userService.getAllUsers();
        Assertions.assertThat(returnUsers.size()).isEqualTo(listOfUsers.size());
        Assertions.assertThat(returnUsers).containsExactlyElementsOf(listOfUsers);


    }

    @Test
    public void when_getUsersWithPagination_then_ReturnUserObject() {
        List<User> users = new ArrayList<>();
        User user1 = User.builder().firstName("Ramesh").lastName("Silva").email("ramesh@gmail.com").build();
        User user2 = User.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build();
        users.add(user1);
        users.add(user2);

        Page<User> pages = new PageImpl<>(users, PageRequest.of(0, 2), users.size());

        when(userRepository.findAll(PageRequest.of(0, 2))).thenReturn(pages);

        Page<User> returnUsers = userService.getUserWithPagination(0, 2);
        Assertions.assertThat(returnUsers.getContent().size()).isEqualTo(users.size());
        Assertions.assertThat(returnUsers).containsExactlyElementsOf(users);
    }

    @Test
    public void when_searchUserByKeyword_then_ReturnsListOfUsers() {
        String q = "keyword";
        List<User> users = new ArrayList<>();
        User user1 = User.builder().firstName("keyword").lastName("Silva").email("ramesh@gmail.com").build();
        User user2 = User.builder().firstName("Tony").lastName("keyword").email("tony@gmail.com").build();
        users.add(user1);
        users.add(user2);

        when(userRepository.searchUsers(q)).thenReturn(users);

        List<User> returnUsers = userService.searchUser(q);
        Assertions.assertThat(returnUsers.size()).isEqualTo(users.size());
        Assertions.assertThat(returnUsers).containsExactlyElementsOf(users);
    }

    @Test
    public void when_searchUserByKeywordWithPaging_then_ReturnsListOfUsers() {
        String q = "keyword";
        List<User> users = new ArrayList<>();
        User user1 = User.builder().firstName("keyword").lastName("Silva").email("ramesh@gmail.com").build();
        User user2 = User.builder().firstName("Tony").lastName("keyword").email("tony@gmail.com").build();
        users.add(user1);
        users.add(user2);
        Page<User> pages = new PageImpl<>(users, PageRequest.of(0, 2), users.size());
        when(userRepository.findByFirstNameContainingOrLastNameContaining(q, q, PageRequest.of(0, 2))).thenReturn(pages);

        Page<User> returnUsers = userService.searchWithPagination(q, PageRequest.of(0, 2));
        Assertions.assertThat(returnUsers.getContent().size()).isEqualTo(users.size());
        Assertions.assertThat(returnUsers).containsExactlyElementsOf(users);

    }

    @Test
    public void whenDeleteUserById_then_verifyRepositoryDeleteMethodCalls() {
        long userId = 1L;
        User user = User.builder().firstName("keyword").lastName("Silva").email("ramesh@gmail.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUserById(userId);
        verify(userRepository).delete(user);
    }

}