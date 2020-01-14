package com.location.game.restapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@SuppressWarnings("deprecation")
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id" }))
public class User {

	public User() {
		// Fixed error: No default constructor for entity when findAll()
	}

	@Id
	@SequenceGenerator(name = "conquer_london_sequence", sequenceName = "dbsequence", initialValue = 10000, allocationSize = 1)
	@GeneratedValue(generator = "conquer_london_sequence")
	private long id;

	@Index(name = "index_user_id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "conquered_points", nullable = false)
	private int conqueredPoints;

}
