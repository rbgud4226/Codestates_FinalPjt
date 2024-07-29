package com.pettalk.wcboard.repository;

import com.pettalk.member.entity.Member;
import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.wcboard.entity.WcBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 0908 specification findAll 사용하기 위해 JpaSpecificationExecutor 상속
 * 기존 태그기반 검색기능 비활성화
 */
public interface WcBoardRepository extends JpaRepository<WcBoard, Long> , JpaSpecificationExecutor<WcBoard> {
    Optional<WcBoard> findById(Long wcboardId);
    WcBoard findByWcboardId(Long wcboardId);

//    Page<WcBoard> findByPostStatus(WcBoard.PostStatus postStatus, PageRequest pageRequest);  혹시 모를.. 게시글 상태로 조회
//    Page<WcBoard> findByWcTagContaining(String wcTag, Pageable pageable);
//    Page<WcBoard> findByAnimalTagContaining(String animalTag, PageRequest pageRequest);
//    Page<WcBoard> findByAreaTagContaining(String areaTag, PageRequest pageRequest);
//    Page<WcBoard> findByMember_MemberIdAndPostStatus(Long memberId, WcBoard.PostStatus complete, Pageable pageable);
    Page<WcBoard> findByMember_MemberIdAndPostStatusIn(Long memberId, List<WcBoard.PostStatus> postStatusList, Pageable pageable);
    Page<WcBoard> findByPetSitter_PetSitterIdAndPostStatusIn(Long memberId, List<WcBoard.PostStatus> postStatusList, Pageable pageable);
    List<WcBoard> findByPetSitter_PetSitterIdAndPostStatusIn(Long petSitterId, List<WcBoard.PostStatus> statuses);

//    List<WcBoard> findByPetSitter_PetSitterIdAndPostStatusIn(Long memberId, List<WcBoard.PostStatus> postStatusList);
//    List<WcBoard> findByMember_MemberIdAndPostStatusIn(Long memberId, List<WcBoard.PostStatus> postStatusList);
//    Page<WcBoard> findByPetSitter_PetSitterId(Long memberId, PageRequest pageRequest);
//    Page<WcBoard> findByMember_MemberId(Long memberId, Pageable pageable);
//    WcBoard findByPostStatusIn(List<WcBoard.PostStatus> wcBoardStatus);
//    List<WcBoard> findByMember_MemberId(Long memberId);

}
