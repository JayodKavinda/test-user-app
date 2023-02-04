package com.example.userapp.service.impl;

import com.example.userapp.entity.User;
import com.example.userapp.exception.ResourceNotFoundException;
import com.example.userapp.payload.UserDto;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        User returnUser =  repository.save(user);
        return mapToDto(returnUser);
    }

    @Override
    public UserDto getUserById(long id) {
        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user", id));
        return mapToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        List<UserDto> returnUsers = new ArrayList<>();
        for(User user: users){
            returnUsers.add(mapToDto(user));
        }
        return returnUsers;
    }

    @Override
    public Page<User> getUserWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page,size));
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user", id));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        User returnUser = repository.save(user);
        return mapToDto(returnUser);
    }

    @Override
    public void deleteUserById(long id) {
        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user", id));
        repository.delete(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = repository.searchUsers(query);
        return users;
    }

    @Override
    public Page<User> searchWithPagination(String query, Pageable pageable) {
        return repository.findByFirstNameContainingOrLastNameContaining(query, query, pageable);
    }

    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto mapToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
