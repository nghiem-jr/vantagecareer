package com.nghiemdd.vantagecareer.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }
}
