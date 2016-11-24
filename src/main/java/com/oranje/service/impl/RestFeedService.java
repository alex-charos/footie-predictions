package com.oranje.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oranje.config.JHipsterProperties;
import com.oranje.domain.Fixture;
import com.oranje.service.FeedService;

@Service
public class RestFeedService implements FeedService {
	private final Logger logger = LoggerFactory.getLogger(RestFeedService.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

	@Inject
    private JHipsterProperties jHipsterProperties;
	
	@Override
	public List<Fixture> retrieveFixtures() {
		HttpHeaders headers = new HttpHeaders();

		headers.set("X-AUTH-TOKEN", jHipsterProperties.getRestWS().getToken());

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> re = restTemplate.exchange(jHipsterProperties.getRestWS().getUrl()+"440/fixtures",
				HttpMethod.GET, entity, Map.class);
		Map<String, Object> map = re.getBody();
		List<Map> fixtureList = (List<Map>) map.get("fixtures");
		
		ResponseEntity<Map> relt = restTemplate.exchange(jHipsterProperties.getRestWS().getUrl()+"440/leagueTable",
				HttpMethod.GET, entity, Map.class);
		Map<String, Object> maplt = relt.getBody();
		
		Map<String, Object> standings = (Map<String,Object>)maplt.get("standings");
		List<Fixture> restFixtures = getFixturesFromRest(fixtureList, standings);
		return restFixtures;
	}
	private List<Map<String,String>> groupStandings(Map<String, Object> standings) {
		List<Map<String,String>> groupped = new ArrayList<Map<String,String>> ();
		for (Object o : standings.values()) {
			List<Map<String,String>> tmp = (List<Map<String,String>> )o;
			for (Map<String,String> group : tmp) {
				groupped.add((Map<String,String> )group);
			}
		}
		return groupped;
	}
	
	private List<Fixture> getFixturesFromRest(List<Map> fixtureList, Map<String, Object> standings) {
		List<Map<String,String>> groupped = groupStandings(standings);
		List<Fixture> fixtures = new ArrayList<Fixture>();
		for (Map m : fixtureList) {
			Map<String, Object> fixture = (Map<String, Object>) m;
			String homeTeam = (String) fixture.get("homeTeamName");
			String awayTeam = (String) fixture.get("awayTeamName");
			String id = (String) ((Map<String, Object>) ((Map<String, Object>) fixture.get("_links")).get("self"))
					.get("href");
			id = id.substring(id.lastIndexOf("/") + 1);
			String date = (String) fixture.get("date");
			String group = getGroup(homeTeam, awayTeam, groupped);
			Integer tmpMatchDay = (Integer) fixture.get("matchday");
			Integer matchDay = tmpMatchDay!=null?  tmpMatchDay  : null;
			String homeBadge = getBadge(homeTeam, groupped);
			String awayBadge = getBadge(awayTeam, groupped);
			Date d = null;
			try {
				d = sdf.parse(date);
				logger.warn(d.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Fixture f = new Fixture();
			f.setFdId(id);
			f.setHome(homeTeam);
			f.setAway(awayTeam);
			f.setKickOff(ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault()));
			f.setGroup(group);
			f.setOrder(matchDay);
			f.setHomeFlag(homeBadge);
			f.setAwayFlag(awayBadge);
			fixtures.add(f);

		}

		return fixtures;
	}
	
	private String getGroup(String homeTeam, String awayTeam, List<Map<String,String>> groups ) {
		String homeGroup = null;
		String awayGroup = null;
		
		for (Map<String,String> teamG : groups) {
			if (teamG.get("team").equals(homeTeam)) {
				homeGroup = teamG.get("group");
			}
			if (teamG.get("team").equals(awayTeam)) {
				awayGroup = teamG.get("group");
			}
		}
		
		if(homeGroup !=null && awayGroup!= null && 
			homeGroup.equals(awayGroup)) {
			return homeGroup;
		}
		return null;
		
	}
	private String getBadge(String team,  List<Map<String,String>> groups ) {
		String badge = null;
		
		
		for (Map<String,String> teamG : groups) {
			if (teamG.get("team").equals(team)) {
				badge = teamG.get("crestURI");
			}
			 
		}
	
		return badge;
		
	}
}
