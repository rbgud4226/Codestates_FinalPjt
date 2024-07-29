package com.pettalk.member.controller;

import com.pettalk.argumentresolver.LoginMemberId;
import com.pettalk.exception.BusinessLogicException;
import com.pettalk.exception.ExceptionCode;
import com.pettalk.member.dto.*;
import com.pettalk.member.entity.Member;
import com.pettalk.member.mapper.MemberMapper;
import com.pettalk.member.repository.MemberRepository;
import com.pettalk.member.service.MemberService;
import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.wcboard.entity.WcBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@Validated

public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MemberRepository memberRepository;
    public MemberController(MemberService memberService, MemberMapper mapper, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.mapper = mapper;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@LoginMemberId Long memberId) {
        try {
            memberService.logoutAndRemoveRefreshToken(memberId);
            return new ResponseEntity<>("로그아웃 완료되었습니다", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping
    public ResponseEntity memberSignUp(@Valid @RequestBody PostMemberDto requestBody) {
        try {
            Member member = memberService.createMember(mapper.memberPostToMember(requestBody));
            return new ResponseEntity<>("회원가입 완료되었습니다", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping
    public ResponseEntity memberGet(@LoginMemberId Long memberId,
                                    @RequestParam int page) {
        try {
            GetMemberDto getMemberDto = memberService.getMember(memberId,page);
            return new ResponseEntity<>(getMemberDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/recent")
//    public ResponseEntity getMemberBoards(@LoginMemberId Long memberId,
//                                          @RequestParam int page) {
//        try {
//            GetMembersDto wcBoardDtoResponses = memberService.getMembers(memberId,page);
//            return new ResponseEntity<>(wcBoardDtoResponses, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//    }

    @GetMapping("/alls")
    public ResponseEntity getMemberBoardsAll(@LoginMemberId @Positive Long memberId) {
        GetMembersDto wcBoards = memberService.getMemberAll(memberId);
        return new ResponseEntity<>(wcBoards, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity memberupdate(@RequestBody PatchMemberDto requestBody,
                                       @LoginMemberId Long memberId){
        try {
            Member member = memberService.updateMember(mapper.memberPatchToMember(requestBody), memberId);
            return new ResponseEntity<>("\"nickname\" : \"" + requestBody.getNickName(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping
    public ResponseEntity memberDelete(@LoginMemberId Long memberId) {
        try {
            memberService.deleteMember(memberId);
            return new ResponseEntity<>("회원 탈퇴가 완료되었습니다", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
  }
