package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.domain.dto.LoginDTO;
import com.nghiemdd.vantagecareer.domain.dto.ResLoginDTO;
import com.nghiemdd.vantagecareer.service.UserService;
import com.nghiemdd.vantagecareer.util.SecurityUtil;
import com.nghiemdd.vantagecareer.util.annotation.ApiMessage;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private SecurityUtil securityUtil;
    private UserService userService;

    @Value("${nghiemdd.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUserame(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // nạp thông tin (nếu xử lý thành công) vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO access_token_dto = new ResLoginDTO();
        User currentUser = userService.handleGetUserByEmail(loginDTO.getUserame());
        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(),
                    currentUser.getName());
            access_token_dto.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(authentication, access_token_dto.getUser());
        access_token_dto.setAccess_token(access_token);

        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUserame(), access_token_dto);
        // update user
        this.userService.updateUserToken(refresh_token, loginDTO.getUserame());

        // set cookies
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).body(access_token_dto);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = userService.handleGetUserByEmail(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setName(currentUser.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("get user by refresh token")
    public ResponseEntity<String> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token) {
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);

        return ResponseEntity.ok().body(refresh_token);
    }
}
