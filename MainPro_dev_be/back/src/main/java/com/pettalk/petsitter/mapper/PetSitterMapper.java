package com.pettalk.petsitter.mapper;


import com.pettalk.petsitter.dto.PetSitterDto;
import com.pettalk.petsitter.entity.PetSitter;
import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.wcboard.entity.WcBoard;
import lombok.Builder;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PetSitterMapper {
    PetSitter postToPetSitter(PetSitterDto.PostDto postDto);
    PetSitter patchToPetSitter(PetSitterDto.PatchDto patchDto);
    PetSitterDto.ResponseDto petSitterToResponse(PetSitter petSitter);

    default List<PetSitterDto.multiResponse> wcBoardstoPetSitterMultiDto (List<WcBoard> wcBoards){
            if ( wcBoards == null ) {
                return null;
            }

            List<PetSitterDto.multiResponse> list = new ArrayList<PetSitterDto.multiResponse>( wcBoards.size() );
            for ( WcBoard wcBoard : wcBoards ) {
                list.add( wcBoardTomultiResponse( wcBoard ) );
            }

            return list;

    };
    private PetSitterDto.multiResponse wcBoardTomultiResponse(WcBoard wcBoard) {
        if ( wcBoard == null ) {
            return null;
        }

        Long wcboardId = null;
        String wcTag = null;
        LocalDateTime startTime = wcBoard.getStartTime();
        LocalDateTime endTime = wcBoard.getEndTime();
//        String startTime = null;
//        String endTime = null;
        String memberImage = null;
        String nickName = wcBoard.getMember().getNickName();
        wcboardId = wcBoard.getWcboardId();
        wcTag = wcBoard.getWcTag();
        WcBoard.PostStatus postStatus = wcBoard.getPostStatus();
//        if ( wcBoard.getStartTime() != null ) {
//            startTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( wcBoard.getStartTime() );
//        }
//        if ( wcBoard.getEndTime() != null ) {
//            endTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( wcBoard.getEndTime() );
//        }


        memberImage = wcBoard.getMember().getProfileImage();

        PetSitterDto.multiResponse multiResponse = new PetSitterDto.multiResponse( wcboardId, wcTag, nickName, startTime, endTime, memberImage, postStatus);

        return multiResponse;
    }

}
