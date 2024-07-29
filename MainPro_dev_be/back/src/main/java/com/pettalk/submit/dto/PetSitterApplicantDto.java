package com.pettalk.submit.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PetSitterApplicantDto {
    @Getter
    @Setter
    public static class petSitterApplicantResponse {
        private String petSitterImage;
        private String name;
        private String nowJob;
        private boolean smoking;
        private String phone;
        private String email;
        private List<String> exAnimal;
        private String info;
        private Long petSitterId;
        //Todo 리뷰 포함되야하나?
    }
}
