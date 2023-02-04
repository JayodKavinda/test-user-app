package com.example.userapp.service.impl;

import com.example.userapp.entity.User;
import com.example.userapp.exception.ResourceNotFoundException;
import com.example.userapp.payload.UserDto;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;


@DataJpaTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

//    @Test
//    public void whenDeleteUser_then_returnNullUser(){
//        long userId = 100L;
//
//
//        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).
//        isThrownBy(()->{userService.getUserById(userId);}).
//        withMessage("user not found");
//
//    }

}