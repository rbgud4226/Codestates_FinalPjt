package com.pettalk.google.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client_id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client_secret}")
    private String clientSecret;

    private String redirectUri = "http://localhost:8080/oauth/google";

    @Value("${jwt.key}")
    private String jwtSecret;
    private final String GOOGLE_API_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";


    public Map<String, Object> getTokenFromAuthorizationCode(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", authorizationCode);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, requestEntity, Map.class);

        return response.getBody();
    }

    public String generateJwtFromGoogle(String googleAccessToken) {
        Map<String, Object> googleProfile = callGoogleApi(googleAccessToken);
        String googleId = String.valueOf(googleProfile.get("id"));
        long now = System.currentTimeMillis();
        long expiration = now + (60 * 60000);

        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());

        String jwtToken = Jwts.builder()
                .setSubject(googleId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();

        return jwtToken;
    }

    public Map<String, Object> getGoogleProfile(String googleAccessToken) {
        return callGoogleApi(googleAccessToken);
    }

    private Map<String, Object> callGoogleApi(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(GOOGLE_API_URL + "?access_token=" + accessToken, Map.class);
        return response.getBody();
    }
}
