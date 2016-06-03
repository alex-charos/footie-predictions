package com.oranje.web.rest;

import com.oranje.Application;
import com.oranje.domain.Prediction;
import com.oranje.repository.PredictionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PredictionResource REST controller.
 *
 * @see PredictionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PredictionResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_CORRECT_SCORES = 1;
    private static final Integer UPDATED_CORRECT_SCORES = 2;

    private static final Integer DEFAULT_CORRECT_RESULTS = 1;
    private static final Integer UPDATED_CORRECT_RESULTS = 2;

    @Inject
    private PredictionRepository predictionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPredictionMockMvc;

    private Prediction prediction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PredictionResource predictionResource = new PredictionResource();
        ReflectionTestUtils.setField(predictionResource, "predictionRepository", predictionRepository);
        this.restPredictionMockMvc = MockMvcBuilders.standaloneSetup(predictionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        predictionRepository.deleteAll();
        prediction = new Prediction();
        prediction.setUsername(DEFAULT_USERNAME);
        prediction.setPoints(DEFAULT_POINTS);
        prediction.setCorrectScores(DEFAULT_CORRECT_SCORES);
        prediction.setCorrectResults(DEFAULT_CORRECT_RESULTS);
    }

    @Test
    public void createPrediction() throws Exception {
        int databaseSizeBeforeCreate = predictionRepository.findAll().size();

        // Create the Prediction

        restPredictionMockMvc.perform(post("/api/predictions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prediction)))
                .andExpect(status().isCreated());

        // Validate the Prediction in the database
        List<Prediction> predictions = predictionRepository.findAll();
        assertThat(predictions).hasSize(databaseSizeBeforeCreate + 1);
        Prediction testPrediction = predictions.get(predictions.size() - 1);
        assertThat(testPrediction.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPrediction.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testPrediction.getCorrectScores()).isEqualTo(DEFAULT_CORRECT_SCORES);
        assertThat(testPrediction.getCorrectResults()).isEqualTo(DEFAULT_CORRECT_RESULTS);
    }

    @Test
    public void getAllPredictions() throws Exception {
        // Initialize the database
        predictionRepository.save(prediction);

        // Get all the predictions
        restPredictionMockMvc.perform(get("/api/predictions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(prediction.getId())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
                .andExpect(jsonPath("$.[*].correctScores").value(hasItem(DEFAULT_CORRECT_SCORES)))
                .andExpect(jsonPath("$.[*].correctResults").value(hasItem(DEFAULT_CORRECT_RESULTS)));
    }

    @Test
    public void getPrediction() throws Exception {
        // Initialize the database
        predictionRepository.save(prediction);

        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", prediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prediction.getId()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.correctScores").value(DEFAULT_CORRECT_SCORES))
            .andExpect(jsonPath("$.correctResults").value(DEFAULT_CORRECT_RESULTS));
    }

    @Test
    public void getNonExistingPrediction() throws Exception {
        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePrediction() throws Exception {
        // Initialize the database
        predictionRepository.save(prediction);

		int databaseSizeBeforeUpdate = predictionRepository.findAll().size();

        // Update the prediction
        prediction.setUsername(UPDATED_USERNAME);
        prediction.setPoints(UPDATED_POINTS);
        prediction.setCorrectScores(UPDATED_CORRECT_SCORES);
        prediction.setCorrectResults(UPDATED_CORRECT_RESULTS);

        restPredictionMockMvc.perform(put("/api/predictions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prediction)))
                .andExpect(status().isOk());

        // Validate the Prediction in the database
        List<Prediction> predictions = predictionRepository.findAll();
        assertThat(predictions).hasSize(databaseSizeBeforeUpdate);
        Prediction testPrediction = predictions.get(predictions.size() - 1);
        assertThat(testPrediction.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPrediction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPrediction.getCorrectScores()).isEqualTo(UPDATED_CORRECT_SCORES);
        assertThat(testPrediction.getCorrectResults()).isEqualTo(UPDATED_CORRECT_RESULTS);
    }

    @Test
    public void deletePrediction() throws Exception {
        // Initialize the database
        predictionRepository.save(prediction);

		int databaseSizeBeforeDelete = predictionRepository.findAll().size();

        // Get the prediction
        restPredictionMockMvc.perform(delete("/api/predictions/{id}", prediction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prediction> predictions = predictionRepository.findAll();
        assertThat(predictions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
