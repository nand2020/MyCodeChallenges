package com.healthcare.application.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.healthcare.application.model.PatchDependentDTO;

@Component
public class PatchDependentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ArrayList.class.equals(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors) {
		List<PatchDependentDTO> dependentDtoList = (List<PatchDependentDTO>) target;
		if (CollectionUtils.isEmpty(dependentDtoList)) {
			errors.reject("INVALID_REQ", "Invalid request received. Please specify valid request");
			return;
		} else {
			dependentDtoList.forEach(dependentDTO -> {
				if (dependentDTO.getId() == null || dependentDTO.getId() <= 0) {
					errors.reject("INVALID_ID", "Invalid ID! Please specify valid ID");
				}
				if (dependentDTO.getName() == null && StringUtils.isEmpty(dependentDTO.getName().trim())) {
					errors.reject("EMPTY_NAME", "Name cannot be empty! Pls specify valid value");
				}
			});
		}
	}

}
