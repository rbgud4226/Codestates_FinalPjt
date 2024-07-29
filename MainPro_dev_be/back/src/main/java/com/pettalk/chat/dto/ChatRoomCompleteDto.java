package com.pettalk.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCompleteDto {
    @Getter
    @Setter
    public static class request{
        private Long wcboardId;
        private Long petSitterId;
    }

    @Getter
    @Setter
    public static class response{
        private String image;
        private String name;
        private String email;
    }

}
