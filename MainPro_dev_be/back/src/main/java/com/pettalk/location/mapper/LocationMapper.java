package com.pettalk.location.mapper;

import com.pettalk.location.dto.LocationDTO;
import com.pettalk.location.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    //    @Mapping(source = "latitude", target = "latitude")
//    @Mapping(source = "longitude", target = "longitude")
    LocationEntity LocationPostDto(LocationDTO.Post postDto);

    //todo : 전달받은 스트링 위도 경도 데이터를 내보낼때는 Float으로 바꿔서 내보내야됨
    List<LocationDTO.Get> LocationGetDtoToLocationEntity (List<LocationEntity> locationEntities);

    LocationDTO.PostResponse LocationPostResponse(LocationEntity location);
}