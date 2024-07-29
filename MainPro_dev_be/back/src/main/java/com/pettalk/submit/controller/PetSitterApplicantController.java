package com.pettalk.submit.controller;

import com.pettalk.argumentresolver.LoginMemberId;
import com.pettalk.member.service.MemberService;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.submit.mapper.PetSitterApplicantMapper;
import com.pettalk.submit.service.PetSitterApplicantService;
import com.pettalk.submit.service.TimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
@Slf4j
public class PetSitterApplicantController {
    private final PetSitterApplicantMapper mapper;
    private final PetSitterApplicantService service;

    //게시글에 펫시터 신청
    @PostMapping("/{wcboard-id}")
    public ResponseEntity PetSitterSubmit(@LoginMemberId Long memberId,
                                          @Positive @PathVariable("wcboard-id") Long wcboardId){
        return service.submitPetSitter(memberId, wcboardId);
    }



    //신청자 조회
    @GetMapping("/{wcboard-id}")
    public ResponseEntity getPetSitterApplicant(@PathVariable("wcboard-id") @Positive Long wcboardId){
//        WcBoard findPost = findVerifyPost(wcboardId);
//        memberService.findNickName(findPost.getMember().getMemberId());

        List<PetSitterApplicant> petSitterApplicantList = service.findApplicantPetsitter(wcboardId);
        return new ResponseEntity<>(mapper.petSitterApplicantToPetSitterApplicantResponse(petSitterApplicantList), HttpStatus.OK);
    }

//    @PostMapping("/{wcboard-id}")
//    public ResponseEntity PetSitterSubmit(@LoginMemberId Long memberId,
//                                          @Positive @PathVariable("wcboard-id") Long wcboardId,
//                                          @RequestHeader("Authorization") String authorizationHeader) {
//        // 'Authorization' 헤더 값을 가져오기
//        log.info("Authorization Header: " + authorizationHeader);
//
//        // 토큰 추출 (Bearer 스키마가 포함된 경우)
//        String token = extractToken(authorizationHeader);
//
//        // token을 이용한 추가 작업 수행 가능
//
//        return service.submitPetSitter(memberId, wcboardId);
//    }
//
//    // 'Bearer ' 스키마가 포함된 토큰을 추출하는 메서드
//    private String extractToken(String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }


}
