package com.pettalk.chat.mapper;

import com.pettalk.chat.dto.ChatRoomRequestDto;
import com.pettalk.chat.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessage chatRoomRequestToChatMessage(ChatRoomRequestDto chatRoomRequest);
}
