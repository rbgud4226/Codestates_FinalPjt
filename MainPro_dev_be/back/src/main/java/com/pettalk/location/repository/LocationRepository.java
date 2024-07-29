package com.pettalk.location.repository;

import com.pettalk.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findAllBylocationId(Long locationId);
    List<LocationEntity> findAll();

}
