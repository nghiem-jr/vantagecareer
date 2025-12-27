package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.domain.dto.ResultPaginationDTO;
import com.nghiemdd.vantagecareer.service.UserService;
import com.nghiemdd.vantagecareer.util.annotation.ApiMessage;
import com.nghiemdd.vantagecareer.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {

        User newUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deletetUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("khong ton tai id lon hon 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("nghiemdd");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User fetchUser = userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(@Filter Specification<User> spec, Pageable pageable) {
        ResultPaginationDTO users = this.userService.fetchAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User postManUser) {
        User updatedUser = this.userService.handleUpdateUser(postManUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

}
