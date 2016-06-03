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
import com.oranje.domain.Prediction;
import com.oranje.repository.FixtureRepository;
import com.oranje.repository.PredictionRepository;
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

		String result = "1";
		Update update = new Update();
		update.set("homeGoals", tmpStrArr.get(0));
		update.set("awayGoals", tmpStrArr.get(1));
		update.set("hasResult", true);

		if (Integer.parseInt(tmpStrArr.get(0)) == Integer.parseInt(tmpStrArr.get(1))) {
			result = "X";
		} else if (Integer.parseInt(tmpStrArr.get(0)) < Integer.parseInt(tmpStrArr.get(1))) {
			result = "2";
		}
		update.set("result", result);
		Fixture f = fixtureRepository.findOne(id);
		f.setHomeGoals(Integer.parseInt(tmpStrArr.get(0)));
		f.setAwayGoals(Integer.parseInt(tmpStrArr.get(1)));

		fixtureRepository.save(f);

		updateScoreAndPoints(id, tmpStrArr.get(0) + "-" + tmpStrArr.get(1), result);
		return null;

	}

	private void updateScoreAndPoints(String id, String score, String result) {

		List<Prediction> worldCupUserPredictions = predictionRepository.findAll();

		for (Prediction wcp : worldCupUserPredictions) {

			boolean hasChanges = false;

			List<String> tmpStrArr = Arrays.asList(wcp.getResultPerEvent().get(id).split("-"));
			String tmpResult = "1";
			if (Integer.parseInt(tmpStrArr.get(0)) == Integer.parseInt(tmpStrArr.get(1))) {
				tmpResult = "X";
			} else if (Integer.parseInt(tmpStrArr.get(0)) < Integer.parseInt(tmpStrArr.get(1))) {
				tmpResult = "2";
			}

			 

			if (wcp.getResultPerEvent().get(id).equals(score)) {

				hasChanges = true;
				Integer points = wcp.getPoints() + 10;
				Integer correctScores = wcp.getCorrectScores() + 1;
				wcp.setPoints(points);
				wcp.setCorrectScores(correctScores);
				
			 

			} else if (tmpResult.equals(result)) {

				hasChanges = true;
				Integer points = wcp.getPoints() + 1;
				Integer correctResults = wcp.getCorrectResults() + 1;

				wcp.setPoints(points);
				wcp.setCorrectResults(correctResults);

			}

			if (hasChanges) {
				predictionRepository.save(wcp);
			}
		}
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
