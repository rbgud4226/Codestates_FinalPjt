package com.pettalk.chat.mapper;

import com.pettalk.chat.dto.ChatRoomPostDto;
import com.pettalk.chat.dto.ChatRoomResponseDto;
import com.pettalk.chat.entity.ChatRoom;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-25T16:30:41+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.1.jar, environment: Java 11.0.14.1 (Azul Systems, Inc.)"
)
@Component
public class ChatRoomMapperImpl implements ChatRoomMapper {

    @Override
    public ChatRoom chatRoomPostDtoToChatRoom(ChatRoomPostDto chatRoomPostDto) {
        if ( chatRoomPostDto == null ) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setWcBoardId( chatRoomPostDto.getWcBoardId() );
        chatRoom.setMemberId( chatRoomPostDto.getMemberId() );
        chatRoom.setPetSitterId( chatRoomPostDto.getPetSitterId() );

        return chatRoom;
    }

    @Override
    public ChatRoomResponseDto chatRoomToChatRoomResponseDto(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();

        chatRoomResponseDto.setRoomId( chatRoom.getRoomId() );
        chatRoomResponseDto.setMemberId( chatRoom.getMemberId() );
        chatRoomResponseDto.setPetSitterId( chatRoom.getPetSitterId() );
        chatRoomResponseDto.setWcBoardId( chatRoom.getWcBoardId() );

        return chatRoomResponseDto;
    }
}
