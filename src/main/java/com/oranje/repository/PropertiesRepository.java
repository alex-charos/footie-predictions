package com.oranje.repository;

import com.oranje.domain.Properties;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Properties entity.
 */
public interface PropertiesRepository extends MongoRepository<Properties,String> {

}
