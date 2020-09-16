package com.healthcare.application.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.healthcare.application.model.EnrolleeDTO;

@Component
public class EnrolleeValidator implements Validator {

	@Autowired
	DependentValidator dependentValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return ArrayList.class.equals(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors) {
		List<EnrolleeDTO> enrolleeDtoList = (List<EnrolleeDTO>) target;
		if (CollectionUtils.isEmpty(enrolleeDtoList)) {
			errors.reject("INVALID_REQ", "Invalid request received. Please specify valid request");
			return;
		} else {
			enrolleeDtoList.forEach(enrolleeDTO -> {
				if (enrolleeDTO.getName() == null && StringUtils.isEmpty(enrolleeDTO.getName().trim())) {
					errors.reject("EMPTY_NAME", "Name cannot be empty! Pls specify valid value");
				}
				if (enrolleeDTO.getActivationStatus() == null) {
					errors.reject("EMPTY_ACTIVATION_STATUS",
							"Activation Status cannot be empty! Pls specify valid value");
				}
				if (enrolleeDTO.getBirthDate() == null) {
					errors.reject("EMPTY_BIRTH_DATE", "Birth Date cannot be empty! Pls specify valid value");
				}
				if (!CollectionUtils.isEmpty(enrolleeDTO.getDependents())) {
					dependentValidator.validate(enrolleeDTO.getDependents(), errors);
				}
			});
		}
	}

}
