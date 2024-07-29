package com.pettalk.wcboard.mapper;

import com.pettalk.member.entity.Member;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.wcboard.entity.WcBoard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pettalk.wcboard.utils.LocalDateTimeFormatting.formatLocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WcBoardMapper {
//     @Mapping(source = "memberId", target = "member.memberId") // 추후에 member에 맞게 수정
     WcBoard wcBoardPostDtoToWcBoard(WcBoardDto.Post postDto);
     WcBoard wcBoardPatchDtotoWcBoard(WcBoardDto.Patch patchDto);
     //디폴트 구현
     WcBoardDto.PostResponse wcBoardResponseDtoToWcBoard(WcBoard wcBoard);

//     WcBoardDto.GetResponse wcBoardGetResponseDtoToWcBoard (WcBoard wcBoard);



     default WcBoardDto.GetResponse wcBoardGetResponseDtoToWcBoard(WcBoard wcBoard) {
          if ( wcBoard == null ) {
               return null;
          }

          Long wcboardId = null;
          String title = null;
          String content = null;
          String images = null;
          String wcTag = null;
          String animalTag = null;
          String areaTag = null;
          String location = null;
          String postStatus = null;
          LocalDateTime startTime = null;
          LocalDateTime endTime = null;
          String createdAt = null;


          wcboardId = wcBoard.getWcboardId();
          title = wcBoard.getTitle();
          content = wcBoard.getContent();
          images = wcBoard.getImages();
          wcTag = wcBoard.getWcTag();
          animalTag = wcBoard.getAnimalTag();
          areaTag = wcBoard.getAreaTag();
          location = wcBoard.getLocation();
          startTime = wcBoard.getStartTime();
          endTime = wcBoard.getEndTime();

          //createdAt 포매팅 적용후 String 타입으로 변환
          createdAt = wcBoard.getCreatedAt();

          if ( wcBoard.getPostStatus() != null ) {
               postStatus = wcBoard.getPostStatus().name();
          }
          //LocalDateTime 형식일 경우 작성
//          if ( wcBoard.getCreatedAt() != null ) {
//               createdAt = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( wcBoard.getCreatedAt() );
//          }

          String nickName = null;
          Member findMember = wcBoard.getMember();
          nickName = findMember.getNickName();

          Long memberId = null;
          memberId = findMember.getMemberId();

          WcBoardDto.GetResponse response = new WcBoardDto.GetResponse( wcboardId, title, content, images, wcTag, animalTag, areaTag, location, postStatus, startTime, endTime, createdAt, nickName, memberId );

          return response;
     }

     default List<WcBoardDto.Response> wcBoardsResponseDtoToWcBoard(List<WcBoard> wcBoards) {
          if ( wcBoards == null ) {
               return null;
          }

          List<WcBoardDto.Response> list = new ArrayList<WcBoardDto.Response>( wcBoards.size() );
          for ( WcBoard wcBoard : wcBoards ) {
               list.add( wcBoardsResponseDtoToWcBoards( wcBoard ) );
          }

          return list;
     }

     private WcBoardDto.Response wcBoardsResponseDtoToWcBoards(WcBoard wcBoard) {
          if (wcBoard == null) {
               return null;
          }

          WcBoardDto.Response response = new WcBoardDto.Response();
          Member member = wcBoard.getMember();

          response.setWcboardId(wcBoard.getWcboardId());
          response.setTitle(wcBoard.getTitle());
          response.setContent(wcBoard.getContent());
          response.setImages(wcBoard.getImages());
          response.setWcTag(wcBoard.getWcTag());
          response.setAnimalTag(wcBoard.getAnimalTag());
          response.setAreaTag(wcBoard.getAreaTag());
          response.setLocation(wcBoard.getLocation());
          response.setStartTime(formatLocalDateTime(wcBoard.getStartTime()));
          response.setEndTime(formatLocalDateTime(wcBoard.getEndTime()));
          response.setPostStatus(wcBoard.getPostStatus().name());
          response.setCreatedAt(wcBoard.getCreatedAt());
          response.setNickName(member.getNickName());

          return response;
     }


//     private String formatLocalDateTime(LocalDateTime dateTime) {
//          if (dateTime != null) {
//               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//               return dateTime.format(formatter);
//          }
//          return null;
//     }

     default List<WcBoardDto.getMemberResponse> wcBoardsToGetMemberResponse(List<WcBoard> wcBoards) {
          if (wcBoards == null) {
               return null;
          }
          List<WcBoardDto.getMemberResponse> list = new ArrayList<>(wcBoards.size());
          for (WcBoard wcBoard : wcBoards) {
               list.add(wcBoardToGetMemberResponse(wcBoard));
          }
          return list;
     }

     private WcBoardDto.getMemberResponse wcBoardToGetMemberResponse(WcBoard wcBoard) {
          if (wcBoard == null) {
               return null;
          }
          WcBoardDto.getMemberResponse response = new WcBoardDto.getMemberResponse();
          Member member = wcBoard.getMember();
          response.setWcboardId(wcBoard.getWcboardId());
          response.setTitle(wcBoard.getTitle());
          response.setContent(wcBoard.getContent());
          response.setImages(wcBoard.getImages());
          response.setWcTag(wcBoard.getWcTag());
          response.setAnimalTag(wcBoard.getAnimalTag());
          response.setAreaTag(wcBoard.getAreaTag());
          response.setPostStatus(wcBoard.getPostStatus().name());
          response.setStartTime(wcBoard.getStartTime());
          response.setEndTime(wcBoard.getEndTime());
          response.setCreatedAt(wcBoard.getCreatedAt());
          response.setNickName(member.getNickName());
          response.setPetSitterNickname(wcBoard.getPetSitter().getName());
          response.setPetSitterImage(wcBoard.getPetSitter().getPetSitterImage());
          return response;
     }

}
