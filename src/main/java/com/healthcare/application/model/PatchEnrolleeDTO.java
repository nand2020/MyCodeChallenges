package com.healthcare.application.model;

import java.util.Date;

import lombok.Data;

@Data
public class PatchEnrolleeDTO {

	private Integer id;
	private String name;
	private Boolean activationStatus;
	private Date birthDate;
	private String phoneNumber;

}
