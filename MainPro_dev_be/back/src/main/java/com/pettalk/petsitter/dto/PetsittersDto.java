package com.pettalk.petsitter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PetsittersDto {
    private List<PetSitterDto.multiResponse> filteredBoards;
    private long totalBoard;

    public PetsittersDto(List<PetSitterDto.multiResponse> filteredBoards, long totalBoard) {
        this.filteredBoards = filteredBoards;
        this.totalBoard = totalBoard;
    }
}
