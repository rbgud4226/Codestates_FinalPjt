package com.pettalk.chat.repository;

import com.pettalk.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByWcBoardId(Long wcBoardId);
    ChatRoom findByMemberId(Long memberId);

    ChatRoom findByWcBoardIdAndMemberIdOrWcBoardIdAndPetSitterId(Long wcboardId, Long memberId, Long wcboardId2, Long petSitterId);

    List<ChatRoom> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
