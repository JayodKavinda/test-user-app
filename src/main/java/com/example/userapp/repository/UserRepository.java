package com.example.userapp.repository;

import com.example.userapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    @Query("SELECT u FROM User u WHERE " +
            "u.firstName LIKE CONCAT('%',:query, '%')" +
            "Or u.lastName LIKE CONCAT('%', :query, '%')"+
            "Or u.email LIKE CONCAT('%', :query, '%')")
    List<User> searchUsers(String query);

    Page<User> findByFirstNameContainingOrLastNameContaining(String q,String q1, Pageable pageable);


}
