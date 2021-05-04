package com.travel.booking.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Hiker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "name_hiker")
	@NotNull
 @ApiModelProperty(notes = "Name of the Hiker",name="name",required=true,value="test name")
	private String name;
	 @ApiModelProperty(notes = "Age of the Hiker",name="age",required=true,value="25")
	 @Min(1)
	private int ageHiker;

	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAgeHiker() {
		return ageHiker;
	}

	public void setAgeHiker(int ageHiker) {
		this.ageHiker = ageHiker;
	}

}
