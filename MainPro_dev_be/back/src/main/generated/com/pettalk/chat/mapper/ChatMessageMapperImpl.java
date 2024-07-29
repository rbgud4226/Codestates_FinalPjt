package com.pettalk.chat.mapper;

import com.pettalk.chat.dto.ChatRoomRequestDto;
import com.pettalk.chat.entity.ChatMessage;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-25T16:41:33+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.14.1 (Azul Systems, Inc.)"
)
@Component
public class ChatMessageMapperImpl implements ChatMessageMapper {

    @Override
    public ChatMessage chatRoomRequestToChatMessage(ChatRoomRequestDto chatRoomRequest) {
        if ( chatRoomRequest == null ) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setMessage( chatRoomRequest.getMessage() );
        if ( chatRoomRequest.getMemberId() != null ) {
            chatMessage.setMemberId( Long.parseLong( chatRoomRequest.getMemberId() ) );
        }

        return chatMessage;
    }
}
