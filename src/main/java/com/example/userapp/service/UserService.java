package com.example.userapp.service;

import com.example.userapp.entity.User;
import com.example.userapp.payload.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);
    UserDto getUserById(long id);
    List<UserDto> getAllUsers();
    Page<User> getUserWithPagination(int page, int size);
    UserDto updateUser(UserDto userDto, long id);
    void deleteUserById(long id);
    List<User> searchUser(String query);
    Page<User> searchWithPagination(String query, Pageable pageable);
}
