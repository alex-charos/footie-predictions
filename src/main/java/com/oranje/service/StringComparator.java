package com.oranje.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class StringComparator {
	
	public double getCommonCombined(String a, String b) {
		//System.out.println( a + "vs " + b);
		double ad = getCommonCharacters(a, b);
		double bd = getCommonCharacters(b, a);
	//	System.out.println("AD: " + ad);
	//	System.out.println("BD: " + bd);
		return ad+bd;
	}
	private double getCommonCharacters(String a, String b) {
		int common = 1;
		Set<Integer> lastcommonpos = new HashSet<Integer>();
		
		for (int i =0 ; i < a.length();i++) {
			/*int start = 0;
			if (!lastcommonpos.isEmpty()) {
				start = lastcommonpos.get(lastcommonpos.size()-1) +1 ;
			}*/
			for (int j=0; j < b.length() && i<a.length(); j++) {
				if (a.charAt(i) == b.charAt(j)) {
					//common++;
					int counter = 1;
					boolean keepSearching = true;
					while ( keepSearching) {
						i++;
						j++;
						if (j<b.length() && i<a.length() &&a.charAt(i) == b.charAt(j)) {
							common+=1;
						} else {
							keepSearching = false;
						}
						
						
					}
					lastcommonpos.add(i);
					
				} 
			}
		}
		
		/*int counter = 0;
		int bonus = 2;
		System.out.println(lastcommonpos);
		for (int i : lastcommonpos) {
			//System.out.print(a.charAt(i));
			int j = i;
			while (lastcommonpos.contains(++j)) {
				common+=1000;
				bonus*=2;
			}
			bonus = 2;
			
			
		}*/
		return common;// Double.parseDouble(common+"")/Double.parseDouble(a.length()+"");
	}

	
}
