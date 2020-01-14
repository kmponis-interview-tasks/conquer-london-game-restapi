package com.location.game.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.location.game.restapi.model.LocationPoint;

@Repository
public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {

	LocationPoint findByLocationPointNumber(String locationPointNumber);

}
