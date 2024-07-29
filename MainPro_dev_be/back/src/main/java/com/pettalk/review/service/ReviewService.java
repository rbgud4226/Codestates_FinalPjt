package com.pettalk.review.service;

import com.pettalk.chat.entity.ChatRoom;
import com.pettalk.chat.repository.ChatRoomRepository;
import com.pettalk.exception.BusinessLogicException;
import com.pettalk.exception.ExceptionCode;
import com.pettalk.member.entity.Member;
import com.pettalk.member.repository.MemberRepository;
import com.pettalk.member.service.MemberService;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.petsitter.repository.PetSitterRepository;
import com.pettalk.review.dto.ReviewDto;
import com.pettalk.review.entity.Review;
import com.pettalk.review.repository.ReviewRepository;
import com.pettalk.wcboard.entity.WcBoard;
import com.pettalk.wcboard.repository.WcBoardRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ChatRoomRepository chatroomRepository;
    @Autowired
    private PetSitterRepository petSitterRepository;
    @Autowired
    private WcBoardRepository wcBoardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    public void createReview(Long memberId, ReviewDto.Post post) {
//        Member findmember = memberService.findVerifyMember(memberId);
        List<ChatRoom> chatRooms = chatroomRepository.findByMemberIdOrderByCreatedAtDesc(memberId); //젤 최근 채팅방으로 가져옴
        if (chatRooms.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }
        ChatRoom latestChatRoom = chatRooms.get(0);
        Long wcBoardId = latestChatRoom.getWcBoardId();

        Optional<Review> existingReview = reviewRepository.findByWcBoardId(wcBoardId);
        if(existingReview.isPresent()){throw new BusinessLogicException(ExceptionCode.COMMENT_EXISTS);} //한 wcboardid에 후기 여러번 안되게
        WcBoard wcBoard = wcBoardRepository.findById(wcBoardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        if (!wcBoard.getPostStatus().equals(WcBoard.PostStatus.COMPLETE)) { // 상태 완료인거 걸러서
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }
        Long petSitterId = latestChatRoom.getPetSitterId();
        PetSitter petSitter = petSitterRepository.findById(petSitterId) //채팅방에서 펫시터 아이디 가져왔는데 매칭안되있을때 ?? 들왔다나감
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PETSITTER_NOT_FOUND));

        Review newReview = new Review();
        newReview.setContent(post.getContent());
//        newReview.setMemberNickName(petSitter.getName());
        newReview.setStar(post.getStar());
        newReview.setPetSitter(petSitter);
        newReview.setWcBoardId(wcBoardId);
//        newReview.setPetSitterImage(petSitter.getPetSitterImage());
//        newReview.setPetSitterEmail(findmember.getEmail());
        reviewRepository.save(newReview);
    }

    public ReviewDto.petSitterReviews getPetSitterReviews(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Long petSitterId = member.getPetSitter().getPetSitterId();

        List<Review> reviews = reviewRepository.findByPetSitter_PetSitterId(petSitterId);
        Long contents = 0L;
        Float star = 0.0f;
        Long totalReviews = 0L;
        String petSitterName = member.getPetSitter().getName();

        for (Review review : reviews) {
            if (review.getContent() != null) {
                contents++;}
            star= star + review.getStar();
            totalReviews++;
        }

        if (totalReviews > 0) {
            star= star / totalReviews;
        } else {
            star= 0.0f;
        }
        return new ReviewDto.petSitterReviews(petSitterName, contents, totalReviews, star);
    }
}

