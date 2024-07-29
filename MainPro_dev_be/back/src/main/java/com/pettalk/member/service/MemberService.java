package com.pettalk.member.service;

import com.pettalk.exception.BusinessLogicException;
import com.pettalk.exception.ExceptionCode;
import com.pettalk.member.dto.GetMemberDto;
import com.pettalk.member.dto.GetMembersDto;
import com.pettalk.member.entity.Member;
import com.pettalk.member.entity.RefreshToken;
import com.pettalk.member.repository.MemberRepository;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.petsitter.repository.PetSitterRepository;
import com.pettalk.submit.repository.PetSitterApplicantRepository;
import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.wcboard.entity.WcBoard;
import com.pettalk.wcboard.mapper.WcBoardMapper;
//import com.pettalk.wcboard.mapper.WcBoardMapperImpl;
import com.pettalk.wcboard.repository.WcBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final WcBoardRepository wcBoardRepository;
    private final WcBoardMapper wcBoardMapper;
    private final PetSitterApplicantRepository petSitterApplicantRepository;
    private final PetSitterRepository petSitterRepository;

    public Member createMember(Member member) {
        if (verifyExistsEmail(member.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
        ;
        String encrytedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encrytedPassword);
        ;
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    private boolean verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    public Member updateMember(Member member, Long memberId) {

        Member findMember = findVerifyMember(memberId);
        if (member.getNickName().trim().length() < 1) {
            throw new RuntimeException("닉네임이 NULL값 입니다");
        } else {
            Optional.ofNullable(member.getNickName().trim()).ifPresent(nickName -> findMember.setNickName(nickName));
        }
        return memberRepository.save(findMember);
    }


    public GetMemberDto getMember(Long memberId, int page) {
        Member findMember = findVerifyMember(memberId);
        boolean checkPetSitter = findMember.getPetSitter() != null;

        Long petSitterId = null;
        String petSitterProfileImage = null;
        if (checkPetSitter) {
            petSitterId = findMember.getPetSitter().getPetSitterId();
            petSitterProfileImage = findMember.getPetSitter().getMember().getProfileImage();
        }
        int size = 1;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("wcboardId").descending());
        List<WcBoard.PostStatus> wcBoardStatus = Arrays.asList(WcBoard.PostStatus.COMPLETE);
        Page<WcBoard> wcBoards = wcBoardRepository.findByMember_MemberIdAndPostStatusIn(findMember.getMemberId(), wcBoardStatus, pageable);
        List<WcBoardDto.getMemberResponse> wcBoardDtoGet = wcBoardMapper.wcBoardsToGetMemberResponse(wcBoards.getContent());
//        Collections.sort(wcBoardDtoGet, Comparator.comparing(WcBoardDto.getMemberResponse:: getWcboardId));
        return new GetMemberDto(findMember.getNickName(), findMember.getEmail(), findMember.getPhone(), findMember.getProfileImage(), wcBoardDtoGet, checkPetSitter, petSitterId);
    }
//
//    public GetMembersDto getMembers(Long memberId, int page) {
//        Member findMember = findVerifyMember(memberId);
//        int size = 4;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        List<WcBoard.PostStatus> wcBoardStatus = Arrays.asList(WcBoard.PostStatus.COMPLETE, WcBoard.PostStatus.IN_PROGRESS, WcBoard.PostStatus.IN_RESERVATION);
//        Page<WcBoard> wcBoards = wcBoardRepository.findByMember_MemberIdAndPostStatusIn(findMember.getMemberId(), wcBoardStatus, pageable);
//        List<WcBoardDto.getMemberResponse> wcBoardDtoGet = wcBoardMapper.wcBoardsToGetMemberResponse(wcBoards.getContent());
//        Collections.sort(wcBoardDtoGet, Comparator.comparing(WcBoardDto.getMemberResponse::getWcboardId).reversed());
//        return new GetMembersDto(wcBoardDtoGet, wcBoards.getTotalElements());
//    }

    public GetMembersDto getMemberAll(Long memberId) {
        List<PetSitterApplicant> findApplicants = petSitterApplicantRepository.findByMember_MemberId(memberId);
        List<Long> wcBoardIds = findApplicants.stream().map(PetSitterApplicant::getWcboardId).collect(Collectors.toList());

        List<WcBoardDto.WcBoardWithPetSitterInfo> wcBoardsPetSitterInfo = new ArrayList<>();
        for(Long wcBoardId : wcBoardIds){
            WcBoard wcBoard = wcBoardRepository.findById(wcBoardId).orElseThrow();
            PetSitterApplicant petSitterApplicant = petSitterApplicantRepository.findPetSitterApplicantByWcboardId(wcBoardId);
            PetSitter petSitter = petSitterRepository.findById(petSitterApplicant.getPetSitter().getPetSitterId()).orElseThrow();

            WcBoardDto.WcBoardWithPetSitterInfo wcBoardWithPetSitterInfo = new WcBoardDto.WcBoardWithPetSitterInfo();
            wcBoardWithPetSitterInfo.setPostStatus(wcBoard.getPostStatus());
            wcBoardWithPetSitterInfo.setStartTime(wcBoard.getStartTime());
            wcBoardWithPetSitterInfo.setEndTime(wcBoard.getEndTime());
            wcBoardWithPetSitterInfo.setPetSitterNickname(petSitter.getName());
            wcBoardWithPetSitterInfo.setPetSitterImage(petSitter.getPetSitterImage());
            wcBoardsPetSitterInfo.add(wcBoardWithPetSitterInfo);
        }
        long totalBoard = wcBoardsPetSitterInfo.size();
        return new GetMembersDto(wcBoardsPetSitterInfo, totalBoard);
    }


    public void deleteMember(Long memberId) {
        Member findMember = findVerifyMember(memberId);
        if(findMember != null){
            findMember.setCheckMember("회원탈퇴완료");
        }
        memberRepository.save(findMember);
    }

    public void logoutAndRemoveRefreshToken(Long memberId) {
        Member findMember = findVerifyMember(memberId);
        SecurityContextHolder.clearContext(); // 기타 로그아웃 관련 처리를 수행합니다. 예를 들어, SecurityContext를 클리어하는 등
    }

    public Member findVerifyMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            return null;
        }

        return optionalMember.get();
    }

    public Member findMemberByPrincipal(String principal) {
        Optional<Member> optionalMember = memberRepository.findByEmailOrKakaoIdOrGoogleId(principal, principal, principal);
        if (!optionalMember.isPresent()) {
            return null;
        }
        Member member = optionalMember.get();
        return member;
    }

    public Member findVerifyNickName(Long memberId) {
        Optional<Member> optionalNickName = memberRepository.findById(memberId);

        Member findMembersNickName =
                optionalNickName.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMembersNickName;
    }

    public Member findNickName(Long memberId) {
        return findVerifyNickName(memberId);
    }

}
