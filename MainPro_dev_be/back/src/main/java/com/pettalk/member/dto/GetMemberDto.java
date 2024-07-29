package com.pettalk.member.dto;

import com.pettalk.wcboard.dto.WcBoardDto;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class GetMemberDto {
    @NotBlank
    private String nickName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String profileImage;

    private List<WcBoardDto.getMemberResponse> wcBoardDtoGet;

    private boolean checkPetSitter;

    private Long petSitterId;

    public GetMemberDto(String nickName, String email, String phone, String profileImage, List<WcBoardDto.getMemberResponse> wcBoardDtoGet , boolean checkPetSitter, Long petSitterId) {
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
        this.wcBoardDtoGet = wcBoardDtoGet;
        this.checkPetSitter = checkPetSitter;
        this.petSitterId = petSitterId;
    }
}
