package com.oranje.repository;

import com.oranje.domain.Fixture;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Fixture entity.
 */
public interface FixtureRepository extends MongoRepository<Fixture,String> {

}
