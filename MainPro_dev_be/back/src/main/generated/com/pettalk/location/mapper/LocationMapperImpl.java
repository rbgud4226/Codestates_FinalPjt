package com.pettalk.location.mapper;

import com.pettalk.location.dto.LocationDTO;
import com.pettalk.location.entity.LocationEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-25T16:41:33+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.14.1 (Azul Systems, Inc.)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationEntity LocationPostDto(LocationDTO.Post postDto) {
        if ( postDto == null ) {
            return null;
        }

        LocationEntity locationEntity = new LocationEntity();

        if ( postDto.getLat() != null ) {
            locationEntity.setLat( Float.parseFloat( postDto.getLat() ) );
        }
        if ( postDto.getLon() != null ) {
            locationEntity.setLon( Float.parseFloat( postDto.getLon() ) );
        }

        return locationEntity;
    }

    @Override
    public List<LocationDTO.Get> LocationGetDtoToLocationEntity(List<LocationEntity> locationEntities) {
        if ( locationEntities == null ) {
            return null;
        }

        List<LocationDTO.Get> list = new ArrayList<LocationDTO.Get>( locationEntities.size() );
        for ( LocationEntity locationEntity : locationEntities ) {
            list.add( locationEntityToGet( locationEntity ) );
        }

        return list;
    }

    @Override
    public LocationDTO.PostResponse LocationPostResponse(LocationEntity location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO.PostResponse postResponse = new LocationDTO.PostResponse();

        if ( location.getLat() != null ) {
            postResponse.setLat( location.getLat() );
        }
        if ( location.getLon() != null ) {
            postResponse.setLon( location.getLon() );
        }

        return postResponse;
    }

    protected LocationDTO.Get locationEntityToGet(LocationEntity locationEntity) {
        if ( locationEntity == null ) {
            return null;
        }

        LocationDTO.Get get = new LocationDTO.Get();

        if ( locationEntity.getLat() != null ) {
            get.setLat( String.valueOf( locationEntity.getLat() ) );
        }
        if ( locationEntity.getLon() != null ) {
            get.setLon( String.valueOf( locationEntity.getLon() ) );
        }

        return get;
    }
}
