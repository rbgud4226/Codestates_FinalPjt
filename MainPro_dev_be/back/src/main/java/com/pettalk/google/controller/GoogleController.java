package com.pettalk.google.controller;

import com.pettalk.google.entity.Google;
import com.pettalk.google.repository.GoogleRepository;
import com.pettalk.google.service.GoogleService;
import com.pettalk.member.entity.Member;
import com.pettalk.member.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Optional;

@RestController
public class GoogleController {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GoogleService googleService;
    private final MemberRepository memberRepository;
    private final GoogleRepository googleRepository;

    public GoogleController(OAuth2AuthorizedClientService authorizedClientService, GoogleService googleService, MemberRepository memberRepository, GoogleRepository googleRepository) {
        this.authorizedClientService = authorizedClientService;
        this.googleService = googleService;
        this.memberRepository = memberRepository;
        this.googleRepository = googleRepository;
    }

    @PostMapping("/oauth/google")
    public ResponseEntity loginGoogle(@RequestParam Map<String, String> payload) {
        String authorizationCode = payload.get("authorizationCode");

        if (authorizationCode == null || authorizationCode.isEmpty()) {
            return ResponseEntity.badRequest().body("authorizationCode is required");
        }

        Map<String, Object> Token = googleService.getTokenFromAuthorizationCode(authorizationCode);
        String googleAccessToken = (String) Token.get("access_token");
        String refreshToken = (String) Token.get("refresh_token");

        String jwtToken;
        try {
            jwtToken = googleService.generateJwtFromGoogle(googleAccessToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Google 로그인 실패");
        }

        Map<String, Object> googleProfile;
        googleProfile = googleService.getGoogleProfile(googleAccessToken);

        String email = String.valueOf(googleProfile.get("email"));
        String googleId = String.valueOf(googleProfile.get("id"));
        String name = googleProfile.containsKey("name") ? String.valueOf(googleProfile.get("name")) : null;
        Optional<Member> oldMember = memberRepository.findByEmail(email);
        Member checkmember;

        if (!oldMember.isPresent()) {
            checkmember = new Member();
            checkmember.setGoogleId(googleId);
            checkmember.setEmail(email);
            checkmember.setNickName(name);
            memberRepository.save(checkmember);
        } else {
            checkmember = oldMember.get();
        }

        Optional<Google> checkRefresh = googleRepository.findByMember(checkmember);
        if (!checkRefresh.isPresent()) {
            Google googleToken = new Google();
            googleToken.setRefreshToken((String) Token.get("refresh_token"));
            googleToken.setMember(checkmember);
            googleRepository.save(googleToken);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwtToken);
        responseHeaders.set("refresh_Token", refreshToken);

        return ResponseEntity.ok().headers(responseHeaders).body("Google 로그인 성공");
    }
}
