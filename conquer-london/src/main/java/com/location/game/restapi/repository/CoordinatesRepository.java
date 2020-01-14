package com.location.game.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.location.game.restapi.model.Coordinates;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

	@Query(value = 
			"SELECT lp.location_point_number, c.lng, c.lat, ( " 
			+ "    3959 * acos ("
			+ "      cos ( radians(:latitude) )" 
			+ "      * cos( radians( lat ) )"
			+ "      * cos( radians( lng ) - radians(:longitude) )" 
			+ "      + sin ( radians(:latitude) )" 
			+ "      * sin( radians( lat ) )" 
			+ "    )"
			+ "  ) AS distance " 
			+ "FROM coordinates c "
			+ "  INNER JOIN geometry_coordinates gc ON gc.coordinates_id = c.id" 
			+ "  INNER JOIN geometry g ON g.id = gc.geometry_id"
			+ "  INNER JOIN location_point_geometry lpg ON lpg.geometry_id = g.id" 
			+ "  INNER JOIN location_point lp ON lp.id = lpg.location_point_id " 
			+ "HAVING distance < :distance " 
			+ "ORDER BY distance "
			+ "LIMIT 1;", nativeQuery = true)
	List<Object[]> findPointsWithinDistanceByCoordinations(@Param("longitude") String longitude
			, @Param("latitude") String latitude, @Param("distance") int distance);

}
