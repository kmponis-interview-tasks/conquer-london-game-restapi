package com.location.game.restapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@SuppressWarnings("deprecation")
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "coordinates", uniqueConstraints = @UniqueConstraint(columnNames = { "lng", "lat" }))
public class Coordinates {

	public Coordinates() {
		// Fixed error: No default constructor for entity when findAll()
	}

	@JsonIgnore
	@Id
	@SequenceGenerator(name = "conquer_london_sequence", sequenceName = "dbsequence", initialValue = 10000, allocationSize = 1)
	@GeneratedValue(generator = "conquer_london_sequence")
	private long id;

	@Index(name = "index_lng")
	@Column(name = "lng", nullable = false)
	private String lng;

	@Index(name = "index_lat")
	@Column(name = "lat", nullable = false)
	private String lat;

}
