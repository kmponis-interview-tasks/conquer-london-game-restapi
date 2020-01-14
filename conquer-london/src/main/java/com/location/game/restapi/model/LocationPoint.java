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
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "location_point", uniqueConstraints = { @UniqueConstraint(columnNames = { "location_point_number" }) })
public class LocationPoint {

	public LocationPoint() {
		// Fixed error: No default constructor for entity when findAll()
	}

	@Id
	@SequenceGenerator(name = "conquer_london_sequence", sequenceName = "dbsequence", initialValue = 10000, allocationSize = 1)
	@GeneratedValue(generator = "conquer_london_sequence")
	private long id;

	@Column(name = "location_point_number", nullable = false)
	private String locationPointNumber;

	@Column(name = "type", nullable = false)
	private String type;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "location_point_geometry", joinColumns = {
			@JoinColumn(name = "location_point_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "geometry_id", referencedColumnName = "id") })
	private Geometry geometry;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "location_point_properties_info", joinColumns = {
			@JoinColumn(name = "location_point_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "properties_info_id", referencedColumnName = "id") })
	private PropertiesInfo propertiesInfo;

}
