package com.example.userapp.controller;


import com.example.userapp.entity.User;
import com.example.userapp.payload.UserDto;
import com.example.userapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name = "User Controller")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Save new user to database")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get user with give ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users at once")
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get all users with paging")
    @GetMapping("/pages")
    public ResponseEntity<Page<User>> getUsersWithPagination(@RequestParam("page") int page,
                                                                @RequestParam("size") int size){
        return ResponseEntity.ok(userService.getUserWithPagination(page, size));
    }

    @Operation(summary = "Search users with keyword")
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("q") String query){
        return ResponseEntity.ok(userService.searchUser(query));
    }

    @Operation(summary = "Search users with paging")
    @GetMapping("/search/pages")
    public ResponseEntity<Page<User>> searchUsersWithPagination(@RequestParam("q") String query,
                                                                   @RequestParam("page") int page,
                                                                   @RequestParam("size") int size){
        return ResponseEntity.ok(userService.searchWithPagination(query, PageRequest.of(page, size)));
    }

    @Operation(summary = "Update user with give ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") long id, @Valid @RequestBody UserDto userDto){

        return new ResponseEntity<>(userService.updateUser(userDto,id), HttpStatus.OK);
    }

    @Operation(summary = "Delete existing user with given ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {
        //userService.deleteUserById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
