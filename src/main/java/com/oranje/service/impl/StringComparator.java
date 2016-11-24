package com.oranje.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class StringComparator {
	
	public double getCommonCombined(String a, String b) {
		//System.out.println( a + "vs " + b);
		double ad = getCommonCharacters(a, b);
	//	double bd = getCommonCharacters(b, a);
	//	System.out.println("AD: " + ad);
	//	System.out.println("BD: " + bd);
		return (ad);
	}
	private double getCommonCharacters(String a, String b) {
		int common = 1;
		Set<Integer> lastcommonpos = new HashSet<Integer>();
		List<Integer> commonSequences = new ArrayList<Integer>();
		for (int i =0 ; i < a.length();i++) {
			for (int j=0; j < b.length() && i<a.length(); j++) {
				if (a.charAt(i) == b.charAt(j)) {
					//common++;
					int counter = 1;
					boolean keepSearching = true;
					while ( keepSearching) {
						i++;
						j++;
						if (j<b.length() && i<a.length() &&a.charAt(i) == b.charAt(j)) {
							counter++;
							//common+=(counter^counter);
						} else {
							commonSequences.add(counter);
							keepSearching = false;
						}
						
						
					}
					lastcommonpos.add(i);
					
				} 
			}
		}
		
		for (Integer seq : commonSequences) {
			if (seq>1) {
				common*=seq;
			}
		}
		if (b.contains(a) || a.contains(b)) {
			return 1.0;
		}
		
		return Double.parseDouble(common+"")/Double.parseDouble(a.length()+"");
	}

	
}
