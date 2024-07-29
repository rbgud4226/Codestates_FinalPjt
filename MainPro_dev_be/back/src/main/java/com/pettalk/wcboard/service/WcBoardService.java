package com.pettalk.wcboard.service;

import com.pettalk.exception.BusinessLogicException;
import com.pettalk.exception.ExceptionCode;
import com.pettalk.member.repository.MemberRepository;
import com.pettalk.member.service.MemberService;
import com.pettalk.petsitter.service.PetSitterService;
import com.pettalk.wcboard.entity.WcBoard;
import com.pettalk.wcboard.exception.WcBoardException;
import com.pettalk.submit.repository.PetSitterApplicantRepository;
import com.pettalk.wcboard.repository.WcBoardRepository;
import com.pettalk.wcboard.specification.WcBoardSpecification;
import static com.pettalk.wcboard.utils.LocalDateTimeFormatting.formatLocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor //private final 된것만
@Slf4j
public class WcBoardService {
    private final WcBoardRepository wcBoardRepository;
    private final MemberService memberService;

    //구현 주요 로직 로그인한 경우에만 게시글 작성 가능
    // 컨트롤러와 동일하게 멤버검증 부분 테스트와 서버용 분리
    //멤버 검증 로직 포함
    public WcBoard createWcBoardPost (WcBoard wcboard, Long memberId){
        wcboard.setPostStatus(WcBoard.PostStatus.DEFAULT);
        log.info("게시글 작성시 시작시간 : " + wcboard.getStartTime());
        log.info("게시글 작성시 종료시간 : " + wcboard.getEndTime());
        if (memberId == null){
            throw new WcBoardException("로그인!");
        }
        wcboard.setMember(memberService.findVerifyMember(memberId)); // 게시글에 멤버아이디 등록
        //Todo 펫시터 아이디 가져오기
        log.info("게시글 작성시 멤버아이디 : " + wcboard.getMember().getMemberId());
        wcboard.setCreatedAt(formatLocalDateTime(LocalDateTime.now()));
        wcBoardRepository.save(wcboard);
        return wcboard;
    }

    // 구현 주요 로직 : 로그인한 상태라도 본인의 게시글이 아니면 수정 불가
    public WcBoard updateWcBoardPost(WcBoard wcboard, Long memberId) {
//        wcboard.setMember(memberService.findVerifyMember(memberId));    멤버 아이디 가져오기
        WcBoard findPost = findVerifyPost(wcboard.getWcboardId());      //보드 아이디 가져오기
        if (findPost.getMember().getMemberId() == memberId) {
            if (wcboard.getPostStatus() == WcBoard.PostStatus.DEFAULT) {
                Optional.ofNullable(wcboard.getTitle())
                        .ifPresent(title -> findPost.setTitle(title));
                Optional.ofNullable(wcboard.getContent())
                        .ifPresent(content -> findPost.setContent(content)); // TODO : 타이틀과 내용말고 다른것도 수정 추가 필요
            } else {
                throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
            }
            return wcBoardRepository.save(findPost);
        }else {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
    }

    public WcBoard findWcBoardPost(Long wcboardId) {
        WcBoard findPost = findVerifyPost(wcboardId);
        memberService.findNickName(findPost.getMember().getMemberId());
        //추가

        System.out.println(findPost.getMember().getMemberId() + "멤버 아이디 테스트");

        if(wcboardId == null){
            throw new BusinessLogicException(ExceptionCode.POST_NOT_FOUND);
        }else{
            return findVerifyPost(wcboardId);
        }
    }

    //전체 글 조회 (최신순 정렬)
    public Page<WcBoard> findAllPosts(int page, int size) {
        return wcBoardRepository.findAll(PageRequest.of(page, size, Sort.by("wcboardId").descending()));
    }


    //태그사용 조회
    public Page<WcBoard> findAllWithTags(int page, int size, String wcTag, String animalTag, String areaTag) {
        Specification<WcBoard> spec = (root, query, criteriaBuilder) -> null;

        if (wcTag != null)
            spec = spec.and(WcBoardSpecification.equalWcTagWithTag(wcTag));

        if (animalTag != null)
            spec = spec.and(WcBoardSpecification.equalAnimalTagWithTag(animalTag));

        if (areaTag != null)
            spec = spec.and(WcBoardSpecification.equalAreaTagWithTag(areaTag));
//        memberService.findNickName(memberId);
        Pageable pageable = PageRequest.of(page, size);
        return wcBoardRepository.findAll(spec, pageable);
    }

    /**
     * Todo 방어로직
     * 1. 자신이 게시글에 신청할 경우 > 완료
     * 2. 펫시터 아이디가 없는 경우 > 완료
     * 3. 동일한 시간대에 여러게시글에 신청
     */

    /** 0908 태그 다중적용이 안되어 일시 비활성화 처리
    public Page<WcBoard> findPostByWcTag(int page, int size, String wcTag) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("wcboardId").descending());
        return wcBoardRepository.findByWcTagContaining(wcTag, pageRequest);
    }

    public Page<WcBoard> findPostByAnimalTag(int page, int size, String animalTag) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("wcboardId").descending());
        return wcBoardRepository.findByAnimalTagContaining(animalTag, pageRequest);
    }

    public Page<WcBoard> findPostByAreaTag(int page, int size, String areaTag) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("wcboardId").descending());
        return wcBoardRepository.findByAreaTagContaining(areaTag, pageRequest);
    }
     */

    public void deletePost(Long wcboardId, Long memberId) {
        WcBoard findPost = findVerifyPost(wcboardId);

        if(!findPost.getPostStatus().equals(WcBoard.PostStatus.DEFAULT)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED); // 삭제 불가능 예외처리 TODO : 에러코드 수정 필요
        }
        wcBoardRepository.delete(findPost);
    }

    // 게시글 찾고 없으면 예외처리
    public WcBoard findVerifyPost (Long wcboardId) {
        Optional<WcBoard> optionalPOST = wcBoardRepository.findById(wcboardId);

        WcBoard findWcBoardPost =
                optionalPOST.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        return findWcBoardPost;
    }


//    public List<PetSitterApplicant> findApplicantPetsitter(Long wcboardId) {
//        List<PetSitterApplicant> petSitterApplicantList = paRepository.findByWcboardId(wcboardId);
//        return petSitterApplicantList;
//    }
//
//    private boolean isOwnPost(Member member, WcBoard wcBoard) {
//        return member.getMemberId().equals(wcBoard.getMember().getMemberId());
//    }
//
//    private boolean isPetSitter(Member member) {
//        return member.getPetSitter() != null;
//    }
//
//    public boolean petSitterExists(Long petSitterId) {
//        return paRepository.existsById(petSitterId);
//    }


}
