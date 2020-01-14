package com.location.game.restapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "properties_info")
public class PropertiesInfo {

	public PropertiesInfo() {
		// Fixed error: No default constructor for entity when findAll()
	}

	@Id
	@SequenceGenerator(name = "conquer_london_sequence", sequenceName = "dbsequence", initialValue = 10000, allocationSize = 1)
	@GeneratedValue(generator = "conquer_london_sequence")
	private long id;

	@Column(name = "marked", nullable = false)
	private Boolean marked;

}
