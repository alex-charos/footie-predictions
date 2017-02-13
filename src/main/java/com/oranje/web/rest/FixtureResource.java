package com.oranje.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.oranje.domain.Fixture;
import com.oranje.domain.HomeAwayScore;
import com.oranje.domain.Prediction;
import com.oranje.repository.FixtureRepository;
import com.oranje.repository.PredictionRepository;
import com.oranje.service.impl.FixtureUpdater;
import com.oranje.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Fixture.
 */
@RestController
@RequestMapping("/api")
public class FixtureResource {

	private final Logger log = LoggerFactory.getLogger(FixtureResource.class);

	@Inject
	private FixtureRepository fixtureRepository;

	@Inject
	private PredictionRepository predictionRepository;

	
	@Inject
	private FixtureUpdater fixtureUpdater;
	
	/**
	 * GET /fixtures/update -> Update fixtures from ws
	 */
	@RequestMapping(value = "/fixtures/update", method = RequestMethod.GET)
	@Timed
	public void updateFixtures() throws URISyntaxException {
		
		fixtureUpdater.retrieveFixtures();
		
	}
	
	
	/**
	 * POST /fixtures -> Create a new fixture.
	 */
	@RequestMapping(value = "/fixtures", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Fixture> createFixture(@RequestBody Fixture fixture) throws URISyntaxException {
		log.debug("REST request to save Fixture : {}", fixture);
		if (fixture.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("fixture", "idexists", "A new fixture cannot already have an ID"))
					.body(null);
		}
		Fixture result = fixtureRepository.save(fixture);
		return ResponseEntity.created(new URI("/api/fixtures/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("fixture", result.getId().toString())).body(result);
	}

	/**
	 * PUT /fixtures -> Updates an existing fixture.
	 */
	@RequestMapping(value = "/fixtures", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Fixture> updateFixture(@RequestBody Fixture fixture) throws URISyntaxException {
		log.debug("REST request to update Fixture : {}", fixture);
		if (fixture.getId() == null) {
			return createFixture(fixture);
		}
		Fixture result = fixtureRepository.save(fixture);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("fixture", fixture.getId().toString()))
				.body(result);
	}

	/**
	 * GET /fixtures -> get all the fixtures.
	 */
	@RequestMapping(value = "/fixtures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Fixture> getAllFixtures() {
		log.debug("REST request to get all Fixtures");
		return fixtureRepository.findAll();
	}

	/**
	 * GET /fixtures/:id -> get the "id" fixture.
	 */
	@RequestMapping(value = "/fixtures/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Fixture> getFixture(@PathVariable String id) {
		log.debug("REST request to get Fixture : {}", id);
		Fixture fixture = fixtureRepository.findOne(id);
		return Optional.ofNullable(fixture).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/fixtures/update/result/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Fixture> updateFixtureResult(@PathVariable String id, @RequestParam String score) {

		List<String> tmpStrArr = Arrays.asList(score.split("_"));
		Integer homeScore = null;
		Integer awayScore = null;
		
		if (tmpStrArr.size()>=2) {
			homeScore = Integer.parseInt(tmpStrArr.get(0));
			awayScore = Integer.parseInt(tmpStrArr.get(1));
		}
		 
		
		Fixture f = fixtureRepository.findOne(id);
		f.setHomeGoals(homeScore);
		f.setAwayGoals(awayScore);

		fixtureRepository.save(f);

		updateScoreAndPoints(f);
		return null;

	}
	@RequestMapping(value = "/fixtures/update/result/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Fixture> updateAllPoints() {
		updateAllScoreAndPoints();
		return null;

	}
	
	private void updateScoreAndPoints(Fixture f) {

		List<Prediction> predictions = predictionRepository.findAll();

		for (Prediction p : predictions) {
			 
			HomeAwayScore has = p.getResultPerEvent().get(f.getId());
			if (has!=null && has.getHomeScore()!=null && has.getAwayScore()!=null) {
			 
				
				int points = getFixturePoints(f, has);
				if (points == 10) {
					p.setCorrectScores(p.getCorrectScores()+1);
				} else if (points == 1) {
					p.setCorrectResults(p.getCorrectResults()+1);
					
				}
				 
				p.setPoints(p.getPoints()+points);
				 
			}
			
			predictionRepository.save(p);	
		}
		
		
	}
	

	private void updateAllScoreAndPoints( ) {

		List<Prediction> predictions = predictionRepository.findAll();

		for (Prediction p : predictions) {
			int totalPoints = 0;
			int totalCorrectScores = 0;
			int totalCorrectResults = 0;
			for (String id : p.getResultPerEvent().keySet()) {
				HomeAwayScore has = p.getResultPerEvent().get(id);
				Fixture f = fixtureRepository.findOne(id);
				
				int points = getFixturePoints(f, has);
				if (points >= 10) {
					totalCorrectScores++;
				} else if (points == 1) {
					totalCorrectResults++;
				}
				totalPoints+=points;
			}
			
			p.setPoints(totalPoints);
			p.setCorrectResults(totalCorrectResults);
			p.setCorrectScores(totalCorrectScores);
			
			predictionRepository.save(p);
		}
	}
	
	
	private int getFixturePoints(Fixture f, HomeAwayScore has) {
		int points = 0;
		
		
		
		if(has!=null && has.getHomeScore() !=null && has.getAwayScore()!=null && f.getHomeGoals() !=null && f.getAwayGoals() !=null) {
			String predictedResult = null;

			if (has.getHomeScore() > has.getAwayScore()) {
				predictedResult = "1";
			} else if (has.getHomeScore() == has.getAwayScore()) {
				predictedResult = "X";
			} else if (has.getHomeScore() < has.getAwayScore()) {
				predictedResult = "2";
			}
			
			String actualResult = null;
			if (f.getHomeGoals() > f.getAwayGoals()) {
				actualResult = "1";
			} else if (f.getHomeGoals() == f.getAwayGoals()) {
				actualResult = "X";
			} else if (f.getHomeGoals() < f.getAwayGoals()) {
				actualResult = "2";
			}
			
			if (has.getHomeScore().equals(f.getHomeGoals()) && has.getAwayScore().equals(f.getAwayGoals())) {
				points = 10;
				if ("Final".equalsIgnoreCase(f.getGroup()) ) {
					points=15;
				}
				if ( "Semifinal".equalsIgnoreCase(f.getGroup())) {
					points=12;
				}
			} else if (predictedResult.equals(actualResult)) {
				points = 1;
			}
		}
		
		
		return points;

		 
		
	}

	/**
	 * DELETE /fixtures/:id -> delete the "id" fixture.
	 */
	@RequestMapping(value = "/fixtures/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteFixture(@PathVariable String id) {
		log.debug("REST request to delete Fixture : {}", id);
		fixtureRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fixture", id.toString())).build();
	}
}
