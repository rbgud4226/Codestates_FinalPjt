package com.pettalk.chat.mapper;

import com.pettalk.chat.dto.ChatRoomPostDto;
import com.pettalk.chat.dto.ChatRoomResponseDto;
import com.pettalk.chat.entity.ChatRoom;
import com.pettalk.wcboard.entity.WcBoard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoom chatRoomPostDtoToChatRoom(ChatRoomPostDto chatRoomPostDto);
    ChatRoomResponseDto chatRoomToChatRoomResponseDto(ChatRoom chatRoom);
}
