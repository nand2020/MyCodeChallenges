package com.healthcare.application.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.application.dao.model.Enrollee;

@Repository
public interface EnrolleeRepository extends JpaRepository<Enrollee, Integer> {

}
