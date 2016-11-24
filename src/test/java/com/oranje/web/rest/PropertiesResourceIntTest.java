package com.oranje.web.rest;

import com.oranje.Application;
import com.oranje.domain.Properties;
import com.oranje.repository.PropertiesRepository;

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
 * Test class for the PropertiesResource REST controller.
 *
 * @see PropertiesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PropertiesResourceIntTest {


    private static final Boolean DEFAULT_DISPLAY_STANDINGS = false;
    private static final Boolean UPDATED_DISPLAY_STANDINGS = true;

    @Inject
    private PropertiesRepository propertiesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPropertiesMockMvc;

    private Properties properties;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PropertiesResource propertiesResource = new PropertiesResource();
        ReflectionTestUtils.setField(propertiesResource, "propertiesRepository", propertiesRepository);
        this.restPropertiesMockMvc = MockMvcBuilders.standaloneSetup(propertiesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        propertiesRepository.deleteAll();
        properties = new Properties();
        properties.setDisplayStandings(DEFAULT_DISPLAY_STANDINGS);
    }

    @Test
    public void createProperties() throws Exception {
        int databaseSizeBeforeCreate = propertiesRepository.findAll().size();

        // Create the Properties

        restPropertiesMockMvc.perform(post("/api/propertiess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(properties)))
                .andExpect(status().isCreated());

        // Validate the Properties in the database
        List<Properties> propertiess = propertiesRepository.findAll();
        assertThat(propertiess).hasSize(databaseSizeBeforeCreate + 1);
        Properties testProperties = propertiess.get(propertiess.size() - 1);
        assertThat(testProperties.getDisplayStandings()).isEqualTo(DEFAULT_DISPLAY_STANDINGS);
    }

    @Test
    public void getAllPropertiess() throws Exception {
        // Initialize the database
        propertiesRepository.save(properties);

        // Get all the propertiess
        restPropertiesMockMvc.perform(get("/api/propertiess?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(properties.getId())))
                .andExpect(jsonPath("$.[*].displayStandings").value(hasItem(DEFAULT_DISPLAY_STANDINGS.booleanValue())));
    }

    @Test
    public void getProperties() throws Exception {
        // Initialize the database
        propertiesRepository.save(properties);

        // Get the properties
        restPropertiesMockMvc.perform(get("/api/propertiess/{id}", properties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(properties.getId()))
            .andExpect(jsonPath("$.displayStandings").value(DEFAULT_DISPLAY_STANDINGS.booleanValue()));
    }

    @Test
    public void getNonExistingProperties() throws Exception {
        // Get the properties
        restPropertiesMockMvc.perform(get("/api/propertiess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProperties() throws Exception {
        // Initialize the database
        propertiesRepository.save(properties);

		int databaseSizeBeforeUpdate = propertiesRepository.findAll().size();

        // Update the properties
        properties.setDisplayStandings(UPDATED_DISPLAY_STANDINGS);

        restPropertiesMockMvc.perform(put("/api/propertiess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(properties)))
                .andExpect(status().isOk());

        // Validate the Properties in the database
        List<Properties> propertiess = propertiesRepository.findAll();
        assertThat(propertiess).hasSize(databaseSizeBeforeUpdate);
        Properties testProperties = propertiess.get(propertiess.size() - 1);
        assertThat(testProperties.getDisplayStandings()).isEqualTo(UPDATED_DISPLAY_STANDINGS);
    }

    @Test
    public void deleteProperties() throws Exception {
        // Initialize the database
        propertiesRepository.save(properties);

		int databaseSizeBeforeDelete = propertiesRepository.findAll().size();

        // Get the properties
        restPropertiesMockMvc.perform(delete("/api/propertiess/{id}", properties.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Properties> propertiess = propertiesRepository.findAll();
        assertThat(propertiess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
