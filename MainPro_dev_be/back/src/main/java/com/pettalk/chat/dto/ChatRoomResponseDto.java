package com.pettalk.chat.dto;

import com.pettalk.member.entity.Member;
import com.pettalk.petsitter.entity.PetSitter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDto {
    private Long roomId;
    private Long memberId;
    private Long petSitterId;
    private Long wcBoardId;
    @Getter
    @Setter
    public static class acceptDto{
        private String postStatus;
    }
}
