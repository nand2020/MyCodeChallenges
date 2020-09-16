package com.healthcare.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.healthcare.application.dao.model.Dependent;
import com.healthcare.application.dao.model.Enrollee;
import com.healthcare.application.dao.repository.DependentRepository;
import com.healthcare.application.dao.repository.EnrolleeRepository;
import com.healthcare.application.model.DependentDTO;
import com.healthcare.application.model.EnrolleeDTO;
import com.healthcare.application.model.PatchDependentDTO;
import com.healthcare.application.model.PatchEnrolleeDTO;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Service
@Slf4j
@Transactional
public class HealthCareAppService {

	@Autowired
	EnrolleeRepository enrolleeRepository;

	@Autowired
	DependentRepository dependentRepository;

	@Autowired
	MapperFacade mapperFacade;

	public List<EnrolleeDTO> findAllEnrollees() {
		return mapperFacade.mapAsList(enrolleeRepository.findAll(), EnrolleeDTO.class);
	}

	public List<EnrolleeDTO> addNewEnrollees(List<EnrolleeDTO> enrolleeDtoList) {
		List<Enrollee> enrolleeList = mapperFacade.mapAsList(enrolleeDtoList, Enrollee.class);
		enrolleeList = enrolleeRepository.saveAll(enrolleeList);
		return mapperFacade.mapAsList(enrolleeList, EnrolleeDTO.class);

	}

	public boolean deleteEnrollee(Integer id) {
		boolean isDeleted = false;
		try {
			enrolleeRepository.deleteById(id);
			isDeleted = true;
		} catch (Exception e) {
			log.error("Exception occurred while deleting Enrollee: {}", e);
		}
		return isDeleted;
	}

	public List<EnrolleeDTO> updateEnrollees(List<PatchEnrolleeDTO> enrolleeDtoList) {
		List<Enrollee> enrolleeList = mapperFacade.mapAsList(enrolleeDtoList, Enrollee.class);
		List<Enrollee> repoEnrolleeList = enrolleeRepository
				.findAllById(enrolleeList.stream().map(Enrollee::getId).collect(Collectors.toSet()));
		Map<Integer, Enrollee> enrolleeMap = repoEnrolleeList.stream()
				.collect(Collectors.toMap(Enrollee::getId, Function.identity()));
		enrolleeList.forEach(enrolleeToUpdate -> {
			BeanUtils.copyProperties(enrolleeToUpdate, enrolleeMap.get(enrolleeToUpdate.getId()),
					getNullPropertyNames(enrolleeToUpdate));
		});
		repoEnrolleeList = enrolleeRepository.saveAll(enrolleeMap.values());
		return mapperFacade.mapAsList(repoEnrolleeList, EnrolleeDTO.class);
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}

		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public EnrolleeDTO addNewDependents(Integer enrolleeId, List<DependentDTO> dependentDtoList, BindingResult errors) throws BindException {
		Optional<Enrollee> enrolleeOptional = enrolleeRepository.findById(enrolleeId);
		if(!enrolleeOptional.isPresent()) {
			errors.reject("INVALID_ENROLLEE_ID", "Given Enrollee ID is invalid!");
			throw new BindException(errors);
		}
		Enrollee enrollee = enrolleeOptional.get();
		List<Dependent> dependentsList = mapperFacade.mapAsList(dependentDtoList, Dependent.class);
		enrollee.getDependents().addAll(dependentsList);
		enrollee = enrolleeRepository.save(enrollee);
		return mapperFacade.map(enrollee, EnrolleeDTO.class);
	}

	public EnrolleeDTO updateDependents(Integer enrolleeId, List<PatchDependentDTO> dependentDtoList, BindingResult errors) throws BindException {
		Optional<Enrollee> enrolleeOptional = enrolleeRepository.findById(enrolleeId);
		if(!enrolleeOptional.isPresent()) {
			errors.reject("INVALID_ENROLLEE_ID", "Given Enrollee ID is invalid!");
			throw new BindException(errors);
		}
		Enrollee enrollee = enrolleeOptional.get();
		List<Dependent> dependentsList = mapperFacade.mapAsList(dependentDtoList, Dependent.class);
		List<Dependent> repoDependentList = enrollee.getDependents();
		Map<Integer, Dependent> dependentMap = repoDependentList.stream()
				.collect(Collectors.toMap(Dependent::getId, Function.identity()));
		dependentsList.forEach(dependentToUpdate -> {
			BeanUtils.copyProperties(dependentToUpdate, dependentMap.get(dependentToUpdate.getId()),
					getNullPropertyNames(dependentToUpdate));
		});
		enrollee = enrolleeRepository.save(enrollee);
		return mapperFacade.map(enrollee, EnrolleeDTO.class);
	}

	public boolean deleteAllDependents(Integer enrolleeId) {
		boolean isDeleted = false;
		try {
			dependentRepository.deleteByEnrolleeId(enrolleeId);
			isDeleted = true;
		} catch (Exception e) {
			log.error("Exception occurred while deleting Dependent: {}", e);
		}
		return isDeleted;
	}

	public boolean deleteDependentByEnrolleeIdAndDependentId(Integer enrolleeId, Integer dependentId) {
		boolean isDeleted = false;
		try {
			dependentRepository.deleteByDependentIdAndEnrolleeId(dependentId, enrolleeId);
			isDeleted = true;
		} catch (Exception e) {
			log.error("Exception occurred while deleting Dependent: {}", e);
		}
		return isDeleted;
	}

}
