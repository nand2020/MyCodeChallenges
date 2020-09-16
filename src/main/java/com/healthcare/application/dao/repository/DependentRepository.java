package com.healthcare.application.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.application.dao.model.Dependent;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Integer> {
	
	@Modifying
	@Query(value = "DELETE FROM DEPENDENT d WHERE d.ID = :dependentId AND d.ENROLLEE_ID = :enrolleeId", nativeQuery = true)
	void deleteByDependentIdAndEnrolleeId(@Param("dependentId") Integer dependentId, @Param("enrolleeId") Integer enrolleeId);
	
	@Modifying
	@Query(value = "DELETE FROM DEPENDENT d WHERE d.ENROLLEE_ID = :enrolleeId", nativeQuery = true)
	void deleteByEnrolleeId(@Param("enrolleeId") Integer enrolleeId);

}
