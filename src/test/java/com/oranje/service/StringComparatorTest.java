package com.oranje.service;

import org.junit.Test;

import com.oranje.service.impl.StringComparator;

public class StringComparatorTest {
	
	@Test
	public void testComparator(){
		StringComparator sc  = new StringComparator();
		String[] teamsRest = {"Tottenham Hotspur FC", "FC Porto", "FC Bayern München", "FC Rostov", "FC Basel", "Juventus Turin", "Sporting CP", "PSV Eindhoven", "Besiktas JK", "Leicester City FC", "FC Copenhagen", "Olympique Lyonnais", "Dynamo Kyiv", "Arsenal FC", "Club Brugge", "Paris Saint-Germain", "Bor. Mönchengladbach", "FC Barcelona", "GNK Dinamo Zagreb", "Manchester City FC", "Bayer Leverkusen", "Ludogorez Rasgrad", "Club Atlético de Madrid", "Real Madrid CF", "AS Monaco FC", "Celtic FC", "Sevilla FC", "Borussia Dortmund", "CSKA Moscow", "SSC Napoli", "SL Benfica", "Legia Warszawa"};
		String[] teamsDB = {"Man. City", "FC Porto", "FC Bayern München", "Beşiktaş", "Sporting CP", "Tottenham", "Juventus", "Basel", "Monaco", "Dynamo Kyiv", "Arsenal", "Leverkusen", "Rostov", "Dordmund", "Club Brugge", "Dinamo Zagreb", "Paris Saint-Germain", "Barcelona", "Real Madrid", "Atlético", "København", "Mönchengladbach", "Lyon", "Napoli", "Leicester", "Celtic", "PSV", "Sevilla", "Ludogorets", "Legia Warszawa", "CSKA Moskva", "Benfica"};
		//String[] teamsRest = {"Tottenham Hotspur FC", "FC Porto", "FC Bayern München", "FC Rostov", "FC Basel", "Juventus Turin", "Sporting CP", "PSV Eindhoven", "Besiktas JK", "Leicester City FC", "FC Copenhagen", "Olympique Lyonnais", "Dynamo Kyiv", "Arsenal FC", "Club Brugge", "Paris Saint-Germain", "Bor. Mönchengladbach", "FC Barcelona", "GNK Dinamo Zagreb", "Manchester City FC", "Bayer Leverkusen", "Ludogorez Rasgrad", "Club Atlético de Madrid", "Real Madrid CF", "AS Monaco FC", "Celtic FC", "Sevilla FC", "Borussia Dortmund", "CSKA Moscow", "SSC Napoli", "SL Benfica", "Legia Warszawa"};
		//String[] teamsDB = {"Man. City", "FC Porto", "FC Bayern München", "Beşiktaş", "Sporting CP", "Tottenham", "Juventus", "Basel", "Monaco", "Dynamo Kyiv", "Arsenal", "Leverkusen", "Rostov", "Dordmund", "Club Brugge", "Dinamo Zagreb", "Paris Saint-Germain", "Barcelona", "Real Madrid", "Atlético", "København", "Mönchengladbach", "Lyon", "Napoli", "Leicester", "Celtic", "PSV", "Sevilla", "Ludogorets", "Legia Warszawa", "CSKA Moskva", "Benfica"};

	//	String[] teamsRest = { "FC Copenhagen" };
	//	String[] teamsDB = { "København", "Tottenham"  };
	//	sc.getCommonCombined("Legia Warszawa", "Legia Warszawa");
		//sc.getCommonCombined("Bayer Leverkusen", "Leverkusen");
		 
		for (String teamRest : teamsRest) {
			double maxScore = -100000.0;
			String winner = null;
			for (String teamDB : teamsDB) {
				double score = sc.getCommonCombined(teamDB,teamRest  );
				if (score > maxScore) {
					maxScore  =score;
					winner = teamDB;
				}
			//	System.out.println(teamRest + " vs " + teamDB + " : " +score);
			}
			System.out.println("= WINNER = (" +maxScore + ")"+teamRest + " ==== " + winner + " : ");
		}
	}

}
