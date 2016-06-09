package com.oranje.web.rest;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.oranje.domain.Prediction;
import com.oranje.repository.PredictionRepository;
import com.oranje.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Prediction.
 */
@RestController
@RequestMapping("/api")
public class PredictionResource {

    private final Logger log = LoggerFactory.getLogger(PredictionResource.class);
        
    @Inject
    private PredictionRepository predictionRepository;
    
    /**
     * POST  /predictions -> Create a new prediction.
     */
    @RequestMapping(value = "/predictions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prediction> createPrediction(@RequestBody Prediction prediction) throws URISyntaxException {
        log.debug("REST request to save Prediction : {}", prediction);
        if (prediction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("prediction", "idexists", "A new prediction cannot already have an ID")).body(null);
        }
        Prediction result = predictionRepository.save(prediction);
        return ResponseEntity.created(new URI("/api/predictions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prediction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /predictions -> Updates an existing prediction.
     */
    @RequestMapping(value = "/predictions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prediction> updatePrediction(@RequestBody Prediction prediction, Principal principal) throws URISyntaxException {
    	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date rawDate;
		try {
			rawDate = sdf.parse("2016-06-10T12:00:00");
		

		Timestamp ts = new Timestamp(rawDate.getTime()
				+ (TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone("Europe/Athens").getRawOffset()));
		
		
		Timestamp now = new Timestamp( new Date().getTime());
		
		if (now.after(ts)) {
			throw new RuntimeException("DeadLine has passed!");
		}
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	 
    	
        log.debug("REST request to update Prediction : {}", prediction);
        prediction.setUsername(principal.getName());
        Prediction p = predictionRepository.findOneByUsername(prediction.getUsername());
        if (p!=null) {
        	prediction.setId(p.getId());
        }
        if (prediction.getId() == null) {
            return createPrediction(prediction);
        }
       
        Prediction result = predictionRepository.save(prediction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prediction", prediction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /predictions -> get all the predictions.
     */
    @RequestMapping(value = "/predictions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prediction> getAllPredictions() {
        log.debug("REST request to get all Predictions");
        return predictionRepository.findAll();
            }

    
    
     
    
    /**
     * GET  /predictions/:username -> get the "username" prediction.
     */
    @RequestMapping(value = "/predictions/{username}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public  Prediction  getPrediction(@PathVariable String username) {
        log.debug("REST request to get Prediction : {}", username);
        Prediction p = predictionRepository.findOneByUsername(username);
        
        if (p==null) {
        	p = new Prediction();
        }
        return p;
    }

    
    /**
     * DELETE  /predictions/:id -> delete the "id" prediction.
     */
    @RequestMapping(value = "/predictions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrediction(@PathVariable String id) {
        log.debug("REST request to delete Prediction : {}", id);
        predictionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prediction", id.toString())).build();
    }
}
