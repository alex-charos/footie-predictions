package com.oranje.web.rest;

import com.oranje.Application;
import com.oranje.domain.Fixture;
import com.oranje.repository.FixtureRepository;

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
 * Test class for the FixtureResource REST controller.
 *
 * @see FixtureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FixtureResourceIntTest {

    private static final String DEFAULT_HOME = "AAAAA";
    private static final String UPDATED_HOME = "BBBBB";
    private static final String DEFAULT_AWAY = "AAAAA";
    private static final String UPDATED_AWAY = "BBBBB";

    private static final Integer DEFAULT_HOME_GOALS = 1;
    private static final Integer UPDATED_HOME_GOALS = 2;

    private static final Integer DEFAULT_AWAY_GOALS = 1;
    private static final Integer UPDATED_AWAY_GOALS = 2;

    private static final Boolean DEFAULT_HAS_RESULT = false;
    private static final Boolean UPDATED_HAS_RESULT = true;
    private static final String DEFAULT_RESULT = "AAAAA";
    private static final String UPDATED_RESULT = "BBBBB";
    private static final String DEFAULT_GROUP = "AAAAA";
    private static final String UPDATED_GROUP = "BBBBB";
    private static final String DEFAULT_HOME_FLAG = "AAAAA";
    private static final String UPDATED_HOME_FLAG = "BBBBB";
    private static final String DEFAULT_AWAY_FLAG = "AAAAA";
    private static final String UPDATED_AWAY_FLAG = "BBBBB";

    @Inject
    private FixtureRepository fixtureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFixtureMockMvc;

    private Fixture fixture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FixtureResource fixtureResource = new FixtureResource();
        ReflectionTestUtils.setField(fixtureResource, "fixtureRepository", fixtureRepository);
        this.restFixtureMockMvc = MockMvcBuilders.standaloneSetup(fixtureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fixtureRepository.deleteAll();
        fixture = new Fixture();
        fixture.setHome(DEFAULT_HOME);
        fixture.setAway(DEFAULT_AWAY);
        fixture.setHomeGoals(DEFAULT_HOME_GOALS);
        fixture.setAwayGoals(DEFAULT_AWAY_GOALS);
        fixture.setHasResult(DEFAULT_HAS_RESULT);
        fixture.setResult(DEFAULT_RESULT);
        fixture.setGroup(DEFAULT_GROUP);
        fixture.setHomeFlag(DEFAULT_HOME_FLAG);
        fixture.setAwayFlag(DEFAULT_AWAY_FLAG);
    }

    @Test
    public void createFixture() throws Exception {
        int databaseSizeBeforeCreate = fixtureRepository.findAll().size();

        // Create the Fixture

        restFixtureMockMvc.perform(post("/api/fixtures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fixture)))
                .andExpect(status().isCreated());

        // Validate the Fixture in the database
        List<Fixture> fixtures = fixtureRepository.findAll();
        assertThat(fixtures).hasSize(databaseSizeBeforeCreate + 1);
        Fixture testFixture = fixtures.get(fixtures.size() - 1);
        assertThat(testFixture.getHome()).isEqualTo(DEFAULT_HOME);
        assertThat(testFixture.getAway()).isEqualTo(DEFAULT_AWAY);
        assertThat(testFixture.getHomeGoals()).isEqualTo(DEFAULT_HOME_GOALS);
        assertThat(testFixture.getAwayGoals()).isEqualTo(DEFAULT_AWAY_GOALS);
        assertThat(testFixture.getHasResult()).isEqualTo(DEFAULT_HAS_RESULT);
        assertThat(testFixture.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testFixture.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testFixture.getHomeFlag()).isEqualTo(DEFAULT_HOME_FLAG);
        assertThat(testFixture.getAwayFlag()).isEqualTo(DEFAULT_AWAY_FLAG);
    }

    @Test
    public void getAllFixtures() throws Exception {
        // Initialize the database
        fixtureRepository.save(fixture);

        // Get all the fixtures
        restFixtureMockMvc.perform(get("/api/fixtures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fixture.getId())))
                .andExpect(jsonPath("$.[*].home").value(hasItem(DEFAULT_HOME.toString())))
                .andExpect(jsonPath("$.[*].away").value(hasItem(DEFAULT_AWAY.toString())))
                .andExpect(jsonPath("$.[*].homeGoals").value(hasItem(DEFAULT_HOME_GOALS)))
                .andExpect(jsonPath("$.[*].awayGoals").value(hasItem(DEFAULT_AWAY_GOALS)))
                .andExpect(jsonPath("$.[*].hasResult").value(hasItem(DEFAULT_HAS_RESULT.booleanValue())))
                .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
                .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
                .andExpect(jsonPath("$.[*].homeFlag").value(hasItem(DEFAULT_HOME_FLAG.toString())))
                .andExpect(jsonPath("$.[*].awayFlag").value(hasItem(DEFAULT_AWAY_FLAG.toString())));
    }

    @Test
    public void getFixture() throws Exception {
        // Initialize the database
        fixtureRepository.save(fixture);

        // Get the fixture
        restFixtureMockMvc.perform(get("/api/fixtures/{id}", fixture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fixture.getId()))
            .andExpect(jsonPath("$.home").value(DEFAULT_HOME.toString()))
            .andExpect(jsonPath("$.away").value(DEFAULT_AWAY.toString()))
            .andExpect(jsonPath("$.homeGoals").value(DEFAULT_HOME_GOALS))
            .andExpect(jsonPath("$.awayGoals").value(DEFAULT_AWAY_GOALS))
            .andExpect(jsonPath("$.hasResult").value(DEFAULT_HAS_RESULT.booleanValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()))
            .andExpect(jsonPath("$.homeFlag").value(DEFAULT_HOME_FLAG.toString()))
            .andExpect(jsonPath("$.awayFlag").value(DEFAULT_AWAY_FLAG.toString()));
    }

    @Test
    public void getNonExistingFixture() throws Exception {
        // Get the fixture
        restFixtureMockMvc.perform(get("/api/fixtures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFixture() throws Exception {
        // Initialize the database
        fixtureRepository.save(fixture);

		int databaseSizeBeforeUpdate = fixtureRepository.findAll().size();

        // Update the fixture
        fixture.setHome(UPDATED_HOME);
        fixture.setAway(UPDATED_AWAY);
        fixture.setHomeGoals(UPDATED_HOME_GOALS);
        fixture.setAwayGoals(UPDATED_AWAY_GOALS);
        fixture.setHasResult(UPDATED_HAS_RESULT);
        fixture.setResult(UPDATED_RESULT);
        fixture.setGroup(UPDATED_GROUP);
        fixture.setHomeFlag(UPDATED_HOME_FLAG);
        fixture.setAwayFlag(UPDATED_AWAY_FLAG);

        restFixtureMockMvc.perform(put("/api/fixtures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fixture)))
                .andExpect(status().isOk());

        // Validate the Fixture in the database
        List<Fixture> fixtures = fixtureRepository.findAll();
        assertThat(fixtures).hasSize(databaseSizeBeforeUpdate);
        Fixture testFixture = fixtures.get(fixtures.size() - 1);
        assertThat(testFixture.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testFixture.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testFixture.getHomeGoals()).isEqualTo(UPDATED_HOME_GOALS);
        assertThat(testFixture.getAwayGoals()).isEqualTo(UPDATED_AWAY_GOALS);
        assertThat(testFixture.getHasResult()).isEqualTo(UPDATED_HAS_RESULT);
        assertThat(testFixture.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testFixture.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testFixture.getHomeFlag()).isEqualTo(UPDATED_HOME_FLAG);
        assertThat(testFixture.getAwayFlag()).isEqualTo(UPDATED_AWAY_FLAG);
    }

    @Test
    public void deleteFixture() throws Exception {
        // Initialize the database
        fixtureRepository.save(fixture);

		int databaseSizeBeforeDelete = fixtureRepository.findAll().size();

        // Get the fixture
        restFixtureMockMvc.perform(delete("/api/fixtures/{id}", fixture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fixture> fixtures = fixtureRepository.findAll();
        assertThat(fixtures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
