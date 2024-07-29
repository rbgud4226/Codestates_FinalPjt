package com.pettalk.chat.controller;

import com.pettalk.argumentresolver.LoginMemberId;
import com.pettalk.chat.dto.ChatRoomCompleteDto;
import com.pettalk.chat.dto.ChatRoomPostDto;
import com.pettalk.chat.dto.ChatRoomRequestDto;
import com.pettalk.chat.dto.ChatRoomResponseDto;
import com.pettalk.chat.entity.ChatMessage;
import com.pettalk.chat.entity.ChatRoom;
import com.pettalk.chat.exception.ChatRoomException;
import com.pettalk.chat.mapper.ChatMessageMapper;
import com.pettalk.chat.mapper.ChatRoomMapper;
import com.pettalk.chat.repository.ChatMessageRepository;
import com.pettalk.chat.service.ChatMessageService;
import com.pettalk.chat.service.ChatRoomService;
import com.pettalk.member.entity.Member;
import com.pettalk.member.service.MemberService;
import com.pettalk.wcboard.entity.WcBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatRoomController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper chatMessageMapper;
    private final MemberService memberService;

    // 채팅방 생성
    @PostMapping("/chat")
    public ResponseEntity createChatRoom(@RequestBody ChatRoomPostDto chatRoomPostDto,
                                         @LoginMemberId @Positive Long memberId) {
        chatRoomPostDto.setMemberId(memberId);

        log.info(chatRoomPostDto.getMemberId().toString());
        log.info(chatRoomPostDto.getPetSitterId().toString());

        try {
            ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomMapper.chatRoomPostDtoToChatRoom(chatRoomPostDto));
            ChatRoomResponseDto response = chatRoomMapper.chatRoomToChatRoomResponseDto(chatRoom);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

    // 채팅방 불러오기
    @GetMapping("/chat/{wcBoardId}")
    public ResponseEntity getChatRoom(@PathVariable Long wcBoardId,
                                      @LoginMemberId @Positive Long memberId) {
        try {
            ChatRoom chatRoom = chatRoomService.getChatRoom(wcBoardId, memberId);
            ChatRoomResponseDto response = chatRoomMapper.chatRoomToChatRoomResponseDto(chatRoom);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 채팅에서 수락 버튼
    @PostMapping("/chat/accept/{wcboardId}")
    public ResponseEntity ChatAccept(@PathVariable Long wcboardId) {
        chatRoomService.chatAccept(wcboardId);
        return ResponseEntity.ok("수락");
    }

    // 채팅에서 거절 버튼
    @PostMapping("/chat/reject/{wcboardId}")
    public ResponseEntity ChatReject(@PathVariable Long wcboardId) {
        chatRoomService.chatReject(wcboardId);
        return ResponseEntity.ok("거절");
    }

    // 채팅에서 완료 버튼
    @PostMapping("/chat/complete")
    public ResponseEntity ChatComplete(@RequestBody ChatRoomCompleteDto.request completeDto) {
        ChatRoomCompleteDto.response response = chatRoomService.chatComplete(completeDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 채팅방 메세지 조회
    @GetMapping("/message/{roomId}")
    public ResponseEntity getChatHistory(@PathVariable Long roomId) {
        List<ChatMessage> chatHistory = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return new ResponseEntity<>(chatHistory, HttpStatus.OK);
    }


    // 메세지 보내기와 저장
    @MessageMapping("/{roomId}")
    public void message(@DestinationVariable("roomId") Long roomId,
                        ChatRoomRequestDto chatMessage
                        ) {
        log.info(roomId + "sdfgsdfgsdfg");
        log.info(chatMessage.getMessage()+ "sdfgsdfgsdg");
        log.info(chatMessage.getMemberId() + "sdfghsdfgsfg");

        if (!chatRoomService.chatRoomExists(roomId)) {
            log.info("Chat Room not found");
            throw new ChatRoomException("Chat Room not found");
        }
        simpMessageSendingOperations.convertAndSend("/sub/room/" + roomId, chatMessage);

        chatMessageService.createChatMessage(chatMessageMapper.chatRoomRequestToChatMessage(chatMessage), roomId);
    }
}
