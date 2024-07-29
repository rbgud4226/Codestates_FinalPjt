package com.pettalk.submit.repository;

import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.submit.entity.PetSitterApplicant;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetSitterApplicantRepository extends JpaRepository<PetSitterApplicant, Long> {
    List<PetSitterApplicant> findByMember_MemberId(Long memberId);

    List<PetSitterApplicant> findByWcboardId(Long wcboardId);

    List<PetSitterApplicant> findByPetSitter_PetSitterId(Long petSitterId);

    //    Optional<PetSitterApplicant> findPetsitterIdByWcboardId (Long wcboardid);
    Long countByPetSitter_PetSitterId(Long petSitterId);

    @Query("SELECT p FROM PetSitterApplicant p WHERE p.wcboardId = :wcboardId")
    PetSitterApplicant findPetSitterApplicantByWcboardId(@Param("wcboardId") Long wcboardId);


    PetSitter findPetSitterByWcboardId(Long wcboardId);

}
