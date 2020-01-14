package com.location.game.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.location.game.restapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT u.* FROM user u WHERE u.user_id = :userId", nativeQuery = true)
	User findByUserId(@Param("userId") String userId);

}
