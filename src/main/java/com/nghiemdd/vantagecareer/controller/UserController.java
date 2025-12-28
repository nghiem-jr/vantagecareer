package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.domain.dto.ResCreateUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResUpdateUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResultPaginationDTO;
import com.nghiemdd.vantagecareer.service.UserService;
import com.nghiemdd.vantagecareer.util.annotation.ApiMessage;
import com.nghiemdd.vantagecareer.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import io.micrometer.core.instrument.Meter.Id;
import jakarta.validation.Valid;

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
    @ApiMessage("create new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser)
            throws IdInvalidException {
        boolean isEmialExist = this.userService.isEmailExists(postManUser.getEmail());
        if (isEmialExist) {
            throw new IdInvalidException(
                    "Email " + postManUser.getEmail() + " da ton tai, vui long su dung email khac");
        }
        User newUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user by id")
    public ResponseEntity<Void> deletetUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("user với id = " + id + " không tồn tại");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    // cái này cần dùng resuserDTO
    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User fetchUser = userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("user với id = " + id + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(@Filter Specification<User> spec, Pageable pageable) {
        ResultPaginationDTO users = this.userService.fetchAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {
        User existingUser = this.userService.fetchUserById(postManUser.getId());
        if (existingUser == null) {
            throw new IdInvalidException("user với id = " + postManUser.getId() + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(existingUser));
    }

}
