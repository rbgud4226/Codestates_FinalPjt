package com.pettalk.oauth.controller;

import com.pettalk.member.entity.Member;
import com.pettalk.member.entity.RefreshToken;
import com.pettalk.member.repository.MemberRepository;
import com.pettalk.oauth.entity.KakaoRefreshToken;
import com.pettalk.oauth.repository.KakaoRepository;
import com.pettalk.oauth.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class OauthController {

    private KakaoLoginService kakaoLoginService;
    private MemberRepository memberRepository;
    private KakaoRepository kakaoRepository;

    public OauthController(KakaoLoginService kakaoLoginService, MemberRepository memberRepository, KakaoRepository kakaoRepository){
        this.kakaoLoginService = kakaoLoginService;
        this.memberRepository = memberRepository;
        this.kakaoRepository = kakaoRepository;
    }

    @PostMapping("/login")
    public ResponseEntity loginKakaoUnified(@RequestBody Map<String, String> payload) {
        String authorizationCode = payload.get("authorizationCode");

        if (authorizationCode == null || authorizationCode.isEmpty()) {
            return ResponseEntity.badRequest().body("authorizationCode is required");
        }

        Map<String, Object> Token = kakaoLoginService.getTokenFromAuthorizationCode(authorizationCode);
        String kakaoAccessToken = (String) Token.get("access_token");
        String refreshToken = (String) Token.get("refresh_token");

        String jwtToken;
        try {
            jwtToken = kakaoLoginService.generateJwtFromKakao(kakaoAccessToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("카카오 로그인 실패");
        }

        Map<String, Object> kakaoProfile;
        try {
            kakaoProfile = kakaoLoginService.getKakaoProfile(kakaoAccessToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("프로필을 가져오지 못했습니다");
        }

        Member member = new Member();
        member.setKakaoId(String.valueOf(kakaoProfile.get("id")));
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoProfile.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) kakaoProfile.get("properties");
        String email = String.valueOf(kakaoAccount.get("email"));
        Optional<Member> oldMember = memberRepository.findByEmail(email);
        Member checkmember;
        if (!oldMember.isPresent()) {
            checkmember = new Member();
            checkmember.setKakaoId(String.valueOf(kakaoProfile.get("id")));
            checkmember.setNickName(String.valueOf(properties.get("nickname")));
            checkmember.setEmail(email);
            checkmember.setPhone(String.valueOf(kakaoAccount.get("phone_number")));
            checkmember.setProfileImage(String.valueOf(properties.get("profile_image")));

            memberRepository.save(checkmember);
        } else {
            checkmember = oldMember.get();
        }

        Optional<KakaoRefreshToken> checkRefresh = kakaoRepository.findByMember(checkmember);
        if (!checkRefresh.isPresent()) {
            KakaoRefreshToken kakaoRefreshToken = new KakaoRefreshToken();
            kakaoRefreshToken.setRefreshToken((String) Token.get("refresh_token"));
            kakaoRefreshToken.setMember(checkmember);
            kakaoRepository.save(kakaoRefreshToken);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwtToken);
        responseHeaders.set("refresh_Token" , refreshToken);

        return ResponseEntity.ok().headers(responseHeaders).body("kakao 로그인 성공");
    }


    @PostMapping("/newToken/kakao")
    public ResponseEntity refreshJwt(@RequestHeader("RefreshToken") String refreshToken){
        String newJwtToken = kakaoLoginService.generateJwtFromRefreshToken(refreshToken);
        HttpHeaders jwtTokenRefresh = new HttpHeaders();
        jwtTokenRefresh.set("Authorization", "Bearer" + newJwtToken);
        return ResponseEntity.ok().headers(jwtTokenRefresh).body("kakao 토큰 재발급 완료");
    }
}
