package com.healthcare.application.dao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Enrollee {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private Boolean activationStatus;
	private Date birthDate;
	private String phoneNumber;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "enrollee_id")
	private List<Dependent> dependents;

}
