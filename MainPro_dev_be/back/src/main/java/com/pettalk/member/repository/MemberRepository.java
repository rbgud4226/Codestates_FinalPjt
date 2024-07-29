package com.pettalk.member.repository;

import com.pettalk.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    Optional<Member> findByEmailOrKakaoId(String email, String kakaoId);
    Optional<Member> findByEmailOrKakaoIdOrGoogleId(String email, String kakaoId, String googleId);
    Optional<Member> findById(Long memberId);
}
