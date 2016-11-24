package com.oranje.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oranje.domain.Properties;
import com.oranje.repository.PropertiesRepository;
import com.oranje.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Properties.
 */
@RestController
@RequestMapping("/api")
public class PropertiesResource {

    private final Logger log = LoggerFactory.getLogger(PropertiesResource.class);
        
    @Inject
    private PropertiesRepository propertiesRepository;
    
    /**
     * POST  /propertiess -> Create a new properties.
     */
    @RequestMapping(value = "/propertiess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Properties> createProperties(@RequestBody Properties properties) throws URISyntaxException {
        log.debug("REST request to save Properties : {}", properties);
        if (properties.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("properties", "idexists", "A new properties cannot already have an ID")).body(null);
        }
        Properties result = propertiesRepository.save(properties);
        return ResponseEntity.created(new URI("/api/propertiess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("properties", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /propertiess -> Updates an existing properties.
     */
    @RequestMapping(value = "/propertiess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Properties> updateProperties(@RequestBody Properties properties) throws URISyntaxException {
        log.debug("REST request to update Properties : {}", properties);
        if (properties.getId() == null) {
            return createProperties(properties);
        }
        Properties result = propertiesRepository.save(properties);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("properties", properties.getId().toString()))
            .body(result);
    }

    /**
     * GET  /propertiess -> get all the propertiess.
     */
    @RequestMapping(value = "/propertiess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Properties> getAllPropertiess() {
        log.debug("REST request to get all Propertiess");
        return propertiesRepository.findAll();
            }

    /**
     * GET  /propertiess/:id -> get the "id" properties.
     */
    @RequestMapping(value = "/propertiess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Properties> getProperties(@PathVariable String id) {
        log.debug("REST request to get Properties : {}", id);
        Properties properties = propertiesRepository.findOne(id);
        return Optional.ofNullable(properties)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /propertiess/:id -> delete the "id" properties.
     */
    @RequestMapping(value = "/propertiess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProperties(@PathVariable String id) {
        log.debug("REST request to delete Properties : {}", id);
        propertiesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("properties", id.toString())).build();
    }
}
