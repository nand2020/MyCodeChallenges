package com.healthcare.application.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EnrolleeDTO {
	
	private Integer id;
	private String name;
	private Boolean activationStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date birthDate;
	private String phoneNumber;
	private List<DependentDTO> dependents;

}
