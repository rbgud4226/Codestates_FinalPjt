package com.pettalk.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
