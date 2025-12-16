package com.nghiemdd.vantagecareer.service;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    public User fetchUserById(Long userId) {
        Optional<User> userOpt = this.userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            return null;
        }
    }

    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User newUserData) {
        User currenUser = this.fetchUserById(newUserData.getId());
        if (currenUser != null) {
            currenUser.setName(newUserData.getName());
            currenUser.setEmail(newUserData.getEmail());
            currenUser.setPassword(newUserData.getPassword());
            return this.userRepository.save(currenUser);
        } else {
            return null;
        }
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
