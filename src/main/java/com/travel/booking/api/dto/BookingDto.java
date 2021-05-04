package com.travel.booking.api.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.travel.booking.dao.entities.Hiker;

import io.swagger.annotations.ApiModelProperty;

public class BookingDto {

	@ApiModelProperty(notes = "Name of the Trail", name = "name", required = true, value = "Shire")
	@NotBlank
	private String trail;
	@ApiModelProperty(notes = "Date of the Booking", name = "date", required = true, value = "2021/01/01")
	@NotBlank
	private String date;

	@ApiModelProperty(notes = "List of hikers", name = "hikers", required = true, value = "[{ \"name\": \"test test\", \"ageHiker\": 25}]")
	private Set<Hiker> hikers;

	public String getTrail() {
		return trail;
	}

	public void setTrail(String trail) {
		this.trail = trail;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<Hiker> getHikers() {
		return hikers;
	}

	public void setHikers(Set<Hiker> hikers) {
		this.hikers = hikers;
	}

}
