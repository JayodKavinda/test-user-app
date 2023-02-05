package com.example.userapp.repository;

import com.example.userapp.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void whenSaveUser_then_returnUserId() {
        User user1 = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@gmail.com")
                .build();

        userRepository.save(user1);
        Assertions.assertThat(user1.getId()).isPositive();

    }

    @Test
    @Order(2)
    public void whenCallFindById_then_returnCorrectIdUser() {
        User user = userRepository.findById(1L).get();
        Assertions.assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void whenFindAll_then_returnCorrectSize() {
        User user2 = User.builder()
                .firstName("firstNameNew")
                .lastName("lastNameNew")
                .email("emailNew@gmail.com")
                .build();
        userRepository.save(user2);
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(2);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void whenSetNewEmail_then_updateEmail() {
        User user = userRepository.findById(1L).get();
        user.setEmail("changedName@test.com");
        User updatedUser = userRepository.save(user);
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("changedName@test.com");

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void whenDeleteUser_then_returnNullUser() {
        User user = userRepository.findById(1L).get();
        userRepository.delete(user);
        User newUser = null;
        Optional<User> optionalUser = userRepository.findById(1L);
        if (optionalUser.isPresent()) {
            newUser = optionalUser.get();
        }
        Assertions.assertThat(newUser).isNull();

    }

    @Test
    @Order(6)
    public void whenFindByFirstNameContainingOrLastNameContaining_thenReturnPageOfPersons() {
        // Given
        User user1 = User.builder()
                .firstName("firstName1")
                .lastName("lastName1:keyword")
                .email("email1@gmail.com")
                .build();

        User user2 = User.builder()
                .firstName("firstName2:keyword")
                .lastName("lastName2")
                .email("email2@gmail.com")
                .build();
        User user3 = User.builder()
                .firstName("firstName3")
                .lastName("lastName3")
                .email("email3@gmail.com")
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        String keyword = "keyword";

        // Case 01
        Page<User> result = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(keyword, keyword,keyword, PageRequest.of(0, 4));
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(2L);
        Assertions.assertThat(result.getContent()).contains(user1, user2);

        // Case 02
        Page<User> result1 = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(keyword, keyword,keyword, PageRequest.of(0, 1));
        Assertions.assertThat(result1.getNumberOfElements()).isEqualTo(1L);
        Assertions.assertThat(result1.getContent()).contains(user1);


        // Case 03
        Page<User> result2 = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(keyword, keyword,keyword, PageRequest.of(1, 1));
        Assertions.assertThat(result2.getNumberOfElements()).isEqualTo(1L);
        Assertions.assertThat(result2.getContent()).contains(user2);

        // Case 04
        Page<User> result3 = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(keyword, keyword,keyword, PageRequest.of(2, 2));
        Assertions.assertThat(result3.getNumberOfElements()).isZero();
        Assertions.assertThat(result3.getContent()).doesNotContain(user1, user2);
    }

}