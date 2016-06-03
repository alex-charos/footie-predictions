package com.oranje.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oranje.domain.Prediction;

/**
 * Spring Data MongoDB repository for the Prediction entity.
 */
public interface PredictionRepository extends MongoRepository<Prediction,String> {
	
	 Prediction  findOneByUsername(String username);

}
