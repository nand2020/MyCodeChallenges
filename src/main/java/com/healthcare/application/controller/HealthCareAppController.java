package com.healthcare.application.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.healthcare.application.model.DependentDTO;
import com.healthcare.application.model.EnrolleeDTO;
import com.healthcare.application.model.PatchDependentDTO;
import com.healthcare.application.model.PatchEnrolleeDTO;
import com.healthcare.application.service.HealthCareAppService;
import com.healthcare.application.validator.DependentValidator;
import com.healthcare.application.validator.EnrolleeValidator;
import com.healthcare.application.validator.PatchDependentValidator;
import com.healthcare.application.validator.PatchEnrolleeValidator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HealthCareAppController {

	@Autowired
	private HealthCareAppService enrolleeService;

	@Autowired
	private EnrolleeValidator enrolleeValidator;

	@Autowired
	private PatchEnrolleeValidator patchEnrolleeValidator;

	@Autowired
	private DependentValidator dependentValidator;

	@Autowired
	private PatchDependentValidator patchDependentValidator;

	@GetMapping("/findAll")
	public List<EnrolleeDTO> findAllEnrollees() {
		return enrolleeService.findAllEnrollees();
	}

	@PostMapping("/addNewEnrollees")
	public List<EnrolleeDTO> addNewEnrollees(@RequestBody @Valid List<EnrolleeDTO> enrolleeList, BindingResult errors)
			throws BindException {
		log.info("Request received to saveNewEnrollees");
		ValidationUtils.invokeValidator(enrolleeValidator, enrolleeList, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
		List<EnrolleeDTO> enrolleesSavedList = enrolleeService.addNewEnrollees(enrolleeList);
		log.info("Exiting from saveNewEnrollees");
		return enrolleesSavedList;
	}

	@DeleteMapping("/deleteEnrollee/{id}")
	public ResponseEntity<Boolean> deleteEnrollee(@PathVariable("id") @NotNull Integer id) {
		log.info("Request received to delete enrollee");
		if (id == null || id <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Enrollee ID received");
		}
		boolean isEnrolleeDeleted = enrolleeService.deleteEnrollee(id);
		return new ResponseEntity<Boolean>(isEnrolleeDeleted,
				isEnrolleeDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PatchMapping("/updateEnrollees")
	public List<EnrolleeDTO> updateEnrollees(@RequestBody @Valid List<PatchEnrolleeDTO> enrolleeList,
			BindingResult errors) throws BindException {
		ValidationUtils.invokeValidator(patchEnrolleeValidator, enrolleeList, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
		List<EnrolleeDTO> updatedEnrolleeList = enrolleeService.updateEnrollees(enrolleeList);
		return updatedEnrolleeList;
	}

	@PostMapping("/addNewDependents/{enrolleeId}")
	public EnrolleeDTO addNewDependents(@PathVariable("enrolleeId") @NotNull Integer enrolleeId, @RequestBody @Valid List<DependentDTO> dependentList,
			BindingResult errors) throws BindException {
		ValidationUtils.invokeValidator(dependentValidator, dependentList, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
		EnrolleeDTO savedEnrollee = enrolleeService.addNewDependents(enrolleeId, dependentList, errors);
		return savedEnrollee;
	}

	@PatchMapping("/updateDependents/{enrolleeId}")
	public EnrolleeDTO updateDependents(@PathVariable("enrolleeId") @NotNull Integer enrolleeId, @RequestBody @Valid List<PatchDependentDTO> dependentList,
			BindingResult errors) throws BindException {
		ValidationUtils.invokeValidator(patchDependentValidator, dependentList, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
		EnrolleeDTO savedEnrollee = enrolleeService.updateDependents(enrolleeId, dependentList, errors);
		return savedEnrollee;
	}

	@DeleteMapping("/deleteAllDependents/{enroleeId}")
	public ResponseEntity<Boolean> deleteAllDependents(@PathVariable("enroleeId") @NotNull Integer enroleeId)
			throws BindException {
		if (enroleeId == null || enroleeId <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Enrollee ID received");
		}
		boolean areDependentsDeleted = enrolleeService.deleteAllDependents(enroleeId);
		return new ResponseEntity<Boolean>(areDependentsDeleted,
				areDependentsDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/deleteDependent/{enrolleeId}/{dependentId}")
	public ResponseEntity<Boolean> deleteAllDependents(@PathVariable("enrolleeId") @NotNull Integer enrolleeId,
			@PathVariable("dependentId") @NotNull Integer dependentId) throws BindException {
		if (enrolleeId == null || enrolleeId <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Enrollee ID received");
		}
		boolean areDependentsDeleted = enrolleeService.deleteDependentByEnrolleeIdAndDependentId(enrolleeId,
				dependentId);
		return new ResponseEntity<Boolean>(areDependentsDeleted,
				areDependentsDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
