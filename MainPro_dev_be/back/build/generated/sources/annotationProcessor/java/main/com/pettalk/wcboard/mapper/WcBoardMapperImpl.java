package com.pettalk.wcboard.mapper;

import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.wcboard.entity.WcBoard;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-17T03:22:20+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.1.jar, environment: Java 11.0.14.1 (Azul Systems, Inc.)"
)
@Component
public class WcBoardMapperImpl implements WcBoardMapper {

    @Override
    public WcBoard wcBoardPostDtoToWcBoard(WcBoardDto.Post postDto) {
        if ( postDto == null ) {
            return null;
        }

        WcBoard wcBoard = new WcBoard();

        wcBoard.setWcboardId( postDto.getWcboardId() );
        wcBoard.setTitle( postDto.getTitle() );
        wcBoard.setContent( postDto.getContent() );
        wcBoard.setImages( postDto.getImages() );
        wcBoard.setLocation( postDto.getLocation() );
        wcBoard.setWcTag( postDto.getWcTag() );
        wcBoard.setAnimalTag( postDto.getAnimalTag() );
        wcBoard.setAreaTag( postDto.getAreaTag() );
        wcBoard.setCreatedAt( postDto.getCreatedAt() );
        wcBoard.setStartTime( postDto.getStartTime() );
        wcBoard.setEndTime( postDto.getEndTime() );
        if ( postDto.getPostStatus() != null ) {
            wcBoard.setPostStatus( Enum.valueOf( WcBoard.PostStatus.class, postDto.getPostStatus() ) );
        }

        return wcBoard;
    }

    @Override
    public WcBoard wcBoardPatchDtotoWcBoard(WcBoardDto.Patch patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        WcBoard wcBoard = new WcBoard();

        wcBoard.setWcboardId( patchDto.getWcboardId() );
        wcBoard.setTitle( patchDto.getTitle() );
        wcBoard.setContent( patchDto.getContent() );
        wcBoard.setImages( patchDto.getImages() );
        wcBoard.setLocation( patchDto.getLocation() );
        wcBoard.setWcTag( patchDto.getWcTag() );
        wcBoard.setAnimalTag( patchDto.getAnimalTag() );
        wcBoard.setAreaTag( patchDto.getAreaTag() );
        wcBoard.setStartTime( patchDto.getStartTime() );
        wcBoard.setEndTime( patchDto.getEndTime() );
        if ( patchDto.getPostStatus() != null ) {
            wcBoard.setPostStatus( Enum.valueOf( WcBoard.PostStatus.class, patchDto.getPostStatus() ) );
        }

        return wcBoard;
    }

    @Override
    public WcBoardDto.PostResponse wcBoardResponseDtoToWcBoard(WcBoard wcBoard) {
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
        createdAt = wcBoard.getCreatedAt();

        WcBoardDto.PostResponse postResponse = new WcBoardDto.PostResponse( wcboardId, title, content, images, wcTag, animalTag, areaTag, location, startTime, endTime, createdAt );

        return postResponse;
    }
}
