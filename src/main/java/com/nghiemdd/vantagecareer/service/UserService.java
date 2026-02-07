package com.nghiemdd.vantagecareer.service;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nghiemdd.vantagecareer.domain.Company;
import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.domain.dto.Meta;
import com.nghiemdd.vantagecareer.domain.dto.ResCreateUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResUpdateUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResUserDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {

        Page<User> page = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setTotal(page.getTotalElements());
        mt.setPages(page.getTotalPages());

        rs.setMeta(mt);
        // rs.setResult(page.getContent());

        // remove sensitive data
        List<ResUserDTO> listUser = page.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()))
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    // public User handleUpdateUser(User newUserData) {
    // User currenUser = this.fetchUserById(newUserData.getId());
    // if (currenUser != null) {
    // currenUser.setName(newUserData.getName());
    // currenUser.setEmail(newUserData.getEmail());
    // currenUser.setPassword(newUserData.getPassword());
    // return this.userRepository.save(currenUser);
    // } else {
    // return null;
    // }
    // }
    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());

            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

}
