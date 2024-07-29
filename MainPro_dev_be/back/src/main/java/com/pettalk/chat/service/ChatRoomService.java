package com.pettalk.chat.service;

import com.pettalk.chat.dto.ChatRoomCompleteDto;
import com.pettalk.chat.entity.ChatRoom;
import com.pettalk.chat.exception.ChatRoomException;
import com.pettalk.chat.repository.ChatRoomRepository;
import com.pettalk.member.entity.Member;
import com.pettalk.member.service.MemberService;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.petsitter.service.PetSitterService;
import com.pettalk.wcboard.entity.WcBoard;
import com.pettalk.wcboard.repository.WcBoardRepository;
import com.pettalk.wcboard.service.WcBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final WcBoardService wcBoardService;
    private final WcBoardRepository wcBoardRepository;
    private final PetSitterService petSitterService;
    private final MemberService memberService;

    public ChatRoom createChatRoom(ChatRoom chatRoom){
        boolean chatRoomExists = chatRoomRepository.existsByWcBoardId(chatRoom.getWcBoardId());
        if (chatRoomExists){
            throw new ChatRoomException("Chatroom already exists in this board");
        }
        WcBoard wcboard = wcBoardService.findVerifyPost(chatRoom.getWcBoardId());
        wcboard.setPostStatus(WcBoard.PostStatus.IN_RESERVATION);
        wcBoardRepository.save(wcboard);
        return chatRoomRepository.save(chatRoom);
    }

    public boolean chatRoomExists(Long roomId) {
        return chatRoomRepository.existsById(roomId);
    }

    public ChatRoom getChatRoom(Long wcboardId, Long memberId) {
        Long petSitterId = memberService.findVerifyMember(memberId).getPetSitter().getPetSitterId();
        ChatRoom chatRoom = chatRoomRepository.findByWcBoardIdAndMemberIdOrWcBoardIdAndPetSitterId(wcboardId, memberId, wcboardId, petSitterId);
        if(chatRoom == null){
            throw new ChatRoomException("채팅방이 없습니다.");
        }
        return chatRoom;
    }

    public void chatAccept(Long wcBoardId) {
        WcBoard wcBoard = wcBoardService.findVerifyPost(wcBoardId);
        wcBoard.setPostStatus(WcBoard.PostStatus.IN_PROGRESS);
        wcBoardRepository.save(wcBoard);
    }

    public void chatReject(Long wcBoardId) {
        WcBoard wcBoard = wcBoardService.findVerifyPost(wcBoardId);
        wcBoard.setPostStatus(WcBoard.PostStatus.DEFAULT);
        wcBoardRepository.save(wcBoard);
    }

    public ChatRoomCompleteDto.response chatComplete(ChatRoomCompleteDto.request completeDto) {
        WcBoard wcBoard = wcBoardService.findVerifyPost(completeDto.getWcboardId());
        PetSitter petSitter = petSitterService.findPetSitter(completeDto.getPetSitterId());
        String petSitterEmail = memberService.findVerifyMember(petSitter.getMember().getMemberId()).getEmail();
        wcBoard.setPostStatus(WcBoard.PostStatus.COMPLETE);
        wcBoard.setPetSitter(petSitter);
        wcBoardRepository.save(wcBoard);
        ChatRoomCompleteDto.response response = new ChatRoomCompleteDto.response();
        response.setEmail(petSitterEmail);
        response.setImage(petSitter.getPetSitterImage());
        response.setName(petSitter.getName());
        return response;
    }

    public boolean checkSender(Long memberId) {
        ChatRoom chatRoomMember = chatRoomRepository.findByMemberId(memberId);
        Member member = memberService.findVerifyMember(chatRoomMember.getMemberId());
        if(chatRoomMember.getMemberId().equals(memberId)){
            return true;
        }else if(chatRoomMember.getPetSitterId().equals(member.getPetSitter().getPetSitterId())){
            return true;
        }else{
            return false;
        }
    }
}
