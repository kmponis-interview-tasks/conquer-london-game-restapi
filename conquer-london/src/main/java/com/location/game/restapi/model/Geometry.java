package com.location.game.restapi.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "geometry")
public class Geometry {

	public Geometry() {
		// Fixed error: No default constructor for entity when findAll()
	}

	@Id
	@SequenceGenerator(name = "conquer_london_sequence", sequenceName = "dbsequence", initialValue = 10000, allocationSize = 1)
	@GeneratedValue(generator = "conquer_london_sequence")
	private long id;

	@Column(name = "type", nullable = false)
	private String type;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "geometry_coordinates", joinColumns = {
			@JoinColumn(name = "geometry_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "coordinates_id", referencedColumnName = "id") })
	private Coordinates coordinates;

}
