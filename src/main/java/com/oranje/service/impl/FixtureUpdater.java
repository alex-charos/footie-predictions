package com.oranje.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oranje.domain.Fixture;
import com.oranje.repository.FixtureRepository;
import com.oranje.service.FeedService;

@Service
public class FixtureUpdater {
    private final Logger logger = LoggerFactory.getLogger(FixtureUpdater.class);
	
    @Autowired
    FixtureRepository fixtureRepository;
    
    @Autowired
    StringComparator stringComparator;
    
    
    
    @Autowired
    FeedService feedService;
    //@PostConstruct
   // @Scheduled(cron="0 0 20 * * FRI")
	public void retrieveFixtures() {
		List<Fixture> storedFixtures = fixtureRepository.findAll();
		logger.info("retrieving fixtures...");
		List<Fixture> restFixtures = feedService.retrieveFixtures();
		
		//update existing matching fixtures (with lower threshold)
		for (Fixture f : storedFixtures) {
			Fixture restFixture = getFixture(f.getHome(), f.getAway(), Integer.parseInt(f.getFdId()), restFixtures, 1.4);
			if (restFixture !=null) {
				f.setFdId(restFixture.getFdId());
				f.setHome(restFixture.getHome());
				f.setAway(restFixture.getAway());
				f.setKickOff(restFixture.getKickOff());
				f.setGroup(restFixture.getGroup());
				f.setOrder(restFixture.getOrder());
				f.setHomeFlag(restFixture.getHomeFlag());
				f.setAwayFlag(restFixture.getAwayFlag());
				fixtureRepository.save(f);
			} 
			
			
		}
		 storedFixtures = fixtureRepository.findAll();
		//create new fixtures (with higher threshold)
		for (Fixture f : restFixtures) {
			Fixture dbFixture = getFixture(f.getHome(),f.getAway(), Integer.parseInt(f.getFdId()), storedFixtures, 1.8);
			if (dbFixture == null) {
				fixtureRepository.save(f);
			}
		}
		logger.info("retrieving fixtures done...");
	}
	
	
	private Fixture getFixture(String home, String away, Integer feedId, List<Fixture> fixtures, double threshold) {
		double mostLikey = 0.0;
		Fixture candidate = null;
		Set<String> teams = new HashSet<String>();
		for (Fixture f : fixtures) {
			double homePercentage = stringComparator.getCommonCombined(f.getHome(), home);
			double awayPercentage = stringComparator.getCommonCombined(f.getAway(), away);
			double tmpPEr = homePercentage + awayPercentage;
			if (tmpPEr > mostLikey) {
				mostLikey = tmpPEr;
				candidate = f;
			}
			teams.add(f.getHome());
			teams.add(f.getAway());
		}
		if (mostLikey>=threshold) {
			if (candidate.getFdId()!=null && !candidate.getFdId().equals(feedId)) {
				return null;
			}
			return candidate;
		} else {
			logger.warn("Rejected! " + home + " vs " + away + " Could be: " + candidate.getHome() + " vs " + candidate.getAway() + "@ " + mostLikey + " But Threshold not reached ");
		}
		return null;
		
	}
	
	class FeedFixtureQuery {
		String homeTeam;
		String awayTeam;
		String feedId;
	}
}
