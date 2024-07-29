package com.pettalk.chat.service;

import com.pettalk.chat.entity.ChatMessage;
import com.pettalk.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public void createChatMessage(ChatMessage chatMessage, Long roomId){
        chatMessage.setRoomId(roomId);
        chatMessageRepository.save(chatMessage);
    }

}
