package com.healthcare.application.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.healthcare.application.dao.model.Dependent;
import com.healthcare.application.dao.model.Enrollee;
import com.healthcare.application.model.DependentDTO;
import com.healthcare.application.model.EnrolleeDTO;
import com.healthcare.application.model.PatchDependentDTO;
import com.healthcare.application.model.PatchEnrolleeDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
public class AppConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public MapperFacade mapperFacade() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.classMap(Enrollee.class, EnrolleeDTO.class).byDefault().register();
		mapperFactory.classMap(EnrolleeDTO.class, Enrollee.class).byDefault().register();
		mapperFactory.classMap(PatchEnrolleeDTO.class, Enrollee.class).byDefault().register();
		mapperFactory.classMap(Dependent.class, DependentDTO.class).byDefault().register();
		mapperFactory.classMap(DependentDTO.class, Dependent.class).byDefault().register();
		mapperFactory.classMap(PatchDependentDTO.class, Dependent.class).byDefault().register();
		return mapperFactory.getMapperFacade();
	}
	
}
