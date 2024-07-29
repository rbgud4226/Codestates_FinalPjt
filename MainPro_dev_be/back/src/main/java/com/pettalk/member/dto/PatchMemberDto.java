package com.pettalk.member.dto;

import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
public class PatchMemberDto {
    @NotBlank
    private String nickName;
}
