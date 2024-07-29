package com.pettalk.member.dto;

import lombok.Getter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class PostMemberDto {
    @NotBlank
    private String nickName;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String profileImage;
}
