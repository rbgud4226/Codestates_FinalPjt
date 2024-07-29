package com.pettalk.submit.mapper;

import com.pettalk.member.entity.Member;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.submit.dto.PetSitterApplicantDto;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.wcboard.dto.WcBoardDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetSitterApplicantMapper {

    default List<PetSitterApplicantDto.petSitterApplicantResponse> petSitterApplicantToPetSitterApplicantResponse (List<PetSitterApplicant> petSitterApplicant){
        if ( petSitterApplicant == null ) {
            return null;
        }

        List<PetSitterApplicantDto.petSitterApplicantResponse> list = new ArrayList<PetSitterApplicantDto.petSitterApplicantResponse>( petSitterApplicant.size() );
        for ( PetSitterApplicant petSitterApplicant1 : petSitterApplicant ) {
            list.add( petSitterApplicantTopetSitterApplicantResponse( petSitterApplicant1 ) );
        }

        return list;
    }

    private PetSitterApplicantDto.petSitterApplicantResponse petSitterApplicantTopetSitterApplicantResponse(PetSitterApplicant petSitterApplicant) {
        if ( petSitterApplicant == null ) {
            return null;
        }


        PetSitterApplicantDto.petSitterApplicantResponse petSitterApplicantResponse = new PetSitterApplicantDto.petSitterApplicantResponse();

        PetSitter petSitter = petSitterApplicant.getPetSitter();
        Member member = petSitterApplicant.getPetSitter().getMember();

        petSitterApplicantResponse.setPetSitterImage(petSitter.getPetSitterImage());
        petSitterApplicantResponse.setName(petSitter.getName());
        petSitterApplicantResponse.setNowJob(petSitter.getNowJob());
        petSitterApplicantResponse.setSmoking(petSitter.isSmoking());
        petSitterApplicantResponse.setPhone(member.getPhone());
        petSitterApplicantResponse.setEmail(member.getEmail());
        petSitterApplicantResponse.setExAnimal(petSitter.getExAnimal());
        petSitterApplicantResponse.setInfo(petSitter.getInfo());
        petSitterApplicantResponse.setPetSitterId(petSitter.getPetSitterId());

        return petSitterApplicantResponse;
    }
}
