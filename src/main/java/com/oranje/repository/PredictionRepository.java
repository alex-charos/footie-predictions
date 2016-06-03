package com.oranje.repository;

import com.oranje.domain.Prediction;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Prediction entity.
 */
public interface PredictionRepository extends MongoRepository<Prediction,String> {
	
	Iterable<Prediction> findByUsername(String username);

}
