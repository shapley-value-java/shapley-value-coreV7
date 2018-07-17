package org.shapleyvalue.util.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomPermutations {
	
	private static final Logger logger = LoggerFactory.getLogger(RandomPermutations.class);

	
	private static int getRandom(int min, int max) {
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
		return randomNum;
	}
	
	public static List<Integer> getRandom(long size) {
		
		List<Integer> res = new ArrayList<>();
		List<Integer> temp = new ArrayList<>();
		for(int i=1; i<=size; i++) {
			temp.add(i);
		}

		while(!temp.isEmpty()) {
			int random = getRandom(0, temp.size()-1);
			res.add(temp.get(random));
			temp.remove(random);
		}
		
		logger.debug("res {}", res);
		
		return res;
	}

}
