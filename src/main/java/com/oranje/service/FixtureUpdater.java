package com.oranje.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oranje.domain.Fixture;
import com.oranje.repository.FixtureRepository;

@Service
public class FixtureUpdater {

    private final Logger logger = LoggerFactory.getLogger(FixtureUpdater.class);
	
    @Autowired
    FixtureRepository fixtureRepository;
    
    @Autowired
    StringComparator stringComparator;
    
	public void retrieveFixtures() {
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("X-AUTH-TOKEN", "79e23fafd923491b91572cde3c9d41e3");
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> re = 
				restTemplate.exchange("http://api.football-data.org/v1/soccerseasons/440/fixtures",
				HttpMethod.GET, entity, Map.class);
		Map<String,Object> map = re.getBody();
		List<Map> fixtureList = (List<Map>)map.get("fixtures");
		
		List<Fixture> storedFixtures = fixtureRepository.findAll();
		Set<String> teams = new HashSet<String>();
		for (Map m : fixtureList) {
			Map<String, Object> fixture = (Map<String,Object>)m;
			String homeTeam = (String) fixture.get("homeTeamName");
			String awayTeam = (String) fixture.get("awayTeamName");
			String date = (String)fixture.get("date");
			showCommons(homeTeam, awayTeam, storedFixtures);
			//logger.warn(homeTeam + " vs " + awayTeam + " @ " + date);
			teams.add(homeTeam);
			teams.add(awayTeam);
		}
		logger.warn("========TEAMS REST =======");
		logger.warn(teams.toString());
	}
	private void showCommons(String home, String away, List<Fixture> fixtures) {
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
		logger.warn("========TEAMS DB =======");
		logger.warn(teams.toString());
		logger.warn(home + " - " + away + " Has been calculated as : " + candidate.getHome() + " vs " + candidate.getAway() + " with " + mostLikey);
	}
	
	

}
