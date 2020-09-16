package com.healthcare.application.model;

import java.util.Date;

import lombok.Data;

@Data
public class DependentDTO {
	
	private Integer id;
	private String name;
	private Date birthDate;
	private Integer enrolleeId;
	
}
