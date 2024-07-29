package com.pettalk.member.dto;

import com.pettalk.wcboard.dto.WcBoardDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GetMembersDto {
    private List<WcBoardDto.WcBoardWithPetSitterInfo> wcBoardWithPetSitterInfos;
    private long totalBoard;

    public GetMembersDto(List<WcBoardDto.WcBoardWithPetSitterInfo> wcBoardWithPetSitterInfos, long totalBoard) {
        this.wcBoardWithPetSitterInfos = wcBoardWithPetSitterInfos;
        this.totalBoard = totalBoard;
    }
}
