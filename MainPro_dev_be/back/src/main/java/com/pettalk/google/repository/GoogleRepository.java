package com.pettalk.google.repository;

import com.pettalk.google.entity.Google;
import com.pettalk.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleRepository extends JpaRepository<Google, Long> {

    Optional<Google> findByMember(Member member);
}
