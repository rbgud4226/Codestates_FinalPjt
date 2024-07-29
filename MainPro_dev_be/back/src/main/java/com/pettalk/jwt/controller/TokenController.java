package com.pettalk.jwt.controller;

import com.pettalk.jwt.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/newToken")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
           String newAccessToken = tokenService.refreshAccessToken(refreshToken);

            HttpHeaders tokenRefresh = new HttpHeaders();
            tokenRefresh.set("Authorization", "Bearer " + newAccessToken);
            return ResponseEntity.ok().headers(tokenRefresh).body("토큰 재발급 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken이 올바르지 않습니다");
        }
    }
}