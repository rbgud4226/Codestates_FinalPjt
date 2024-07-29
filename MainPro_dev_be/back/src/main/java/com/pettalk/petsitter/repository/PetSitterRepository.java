package com.pettalk.petsitter.repository;

import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.wcboard.entity.WcBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> {

}
