package com.pettalk.jwt.service;

import com.pettalk.jwt.token.JwtTokenizer;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private JwtTokenizer jwtTokenizer;

    public TokenService(JwtTokenizer jwtTokenizer){
        this.jwtTokenizer = jwtTokenizer;
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        Map<String, Object> claims = jwtTokenizer.verifyJwsFromRefreshToken(refreshToken);
        if (claims == null) {
            throw new Exception("유효하지 않은 RefreshToken 입니다");
        }

        Date newExpiration = new Date(System.currentTimeMillis() + 3600000);
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String subject = (String) claims.get("sub");
        Long memberId = (Long) claims.get("memberId");

        String newAccessToken = jwtTokenizer.generateAccessToken(claims, subject, newExpiration, base64EncodedSecretKey, memberId);

        return newAccessToken;
    }
}
