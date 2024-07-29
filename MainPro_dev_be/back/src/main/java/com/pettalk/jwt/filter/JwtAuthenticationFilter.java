package com.pettalk.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pettalk.exception.BusinessLogicException;
import com.pettalk.jwt.token.JwtTokenizer;
import com.pettalk.member.dto.LoginDto;
import com.pettalk.member.entity.Member;
import com.pettalk.member.entity.RefreshToken;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        Member member = (Member) authResult.getPrincipal();
        String accessToken = delegateAccessToken(member);

        Long memberId = member.getMemberId();

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenTime());
        String TokenExpirationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiration);
        response.setHeader("TokenExpiration", TokenExpirationDate);

        String refreshToken = delegateRefreshToken(member);
        String nickName = member.getNickName();
        String profileImage = member.getProfileImage();
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);
        response.setHeader("memberId", String.valueOf(memberId));
        Map<String, Object> responseMessage = new HashMap<>();
        responseMessage.put("nickName", nickName);
        responseMessage.put("profileImage", profileImage);
        String responseBody = new ObjectMapper().writeValueAsString(responseMessage);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        BusinessLogicException businessException = (BusinessLogicException) failed.getCause();
        String exceptionMessage = businessException.getMessage();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("errorMessage", exceptionMessage);
        String responseBody = new ObjectMapper().writeValueAsString(responseMessage);
        response.getWriter().write(responseBody);
    }

    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        String subject = member.getEmail();
        Long memberId = member.getMemberId();

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenTime());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey, memberId);

        return accessToken;
    }


    private String delegateRefreshToken(Member member) {
//        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByMember(member);
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenTime());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String RefreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
// x
//        if (findRefreshToken.isPresent()) {
//            RefreshToken oldRefreshToken = findRefreshToken.get();
//            oldRefreshToken.setToken(newRefreshToken);
//            refreshTokenRepository.save(oldRefreshToken);
//        } else {
//            RefreshToken refreshTokenEntity = new RefreshToken();
//            refreshTokenEntity.setToken(newRefreshToken);
//            refreshTokenEntity.setMember(member);
//            refreshTokenRepository.save(refreshTokenEntity);
//        }
        return RefreshToken;
    }
}
