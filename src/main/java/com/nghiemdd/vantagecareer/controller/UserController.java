package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.service.UserService;
import com.nghiemdd.vantagecareer.service.error.IdInvalidException;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
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
        if(id>1500){
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users=userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    @PutMapping("/users")
    public ResponseEntity<User> updateUser( @RequestBody User postManUser) {
        User updatedUser=this.userService.handleUpdateUser( postManUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

}
