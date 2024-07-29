package com.pettalk.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pettalk.chat.dto.ChatRoom;
//import com.pettalk.chat.entity.ChatEntity;
//import com.pettalk.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSockChatHandler extends TextWebSocketHandler { // TextWebSocketHandler 는 WebSocket 메시지를 텍스트 형식으로 다루는 핸들러
//    private final ObjectMapper objectMapper; // json데이터를 java객체로 변환하거나 java객체를 json 데이터로 변환
////    private final ChatService chatService;
//    @Override
//    // WebSocket 메시지를 처리하는 메서드, 클라이언트로부터 수신된 텍스트 메시지를 처리하고 응답을 생성
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload(); // 수신된 메시지에서 페이로드(실제 데이터) 추출
//        log.info("payload {}", payload);
////        ChatEntity chatEntity = objectMapper.readValue(payload, ChatEntity.class); // 매퍼를 통해 json데이터를 java객체로 변환하면서 entity클래스로 변환
////        ChatRoom room = chatService.findRoomById(chatEntity.getRoomId()); // 생성된 방 찾기
////        room.handleActions(session, chatEntity, chatService);
//    }
//}
