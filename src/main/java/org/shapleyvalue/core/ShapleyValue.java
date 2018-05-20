package org.shapleyvalue.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.shapleyvalue.util.FactorialUtil;
import org.shapleyvalue.util.Permutations;
import org.shapleyvalue.util.permutation.PermutationLinkList;
import org.shapleyvalue.util.permutation.RandomPermutations;

public class ShapleyValue {
	
	private CharacteristicFunction cfunction;
	private List<Double> output;
	private PermutationLinkList permutations;
	private long currentRange;
	private int size;
	private long factorialSize;
	
	private final Logger logger = LoggerFactory.getLogger(ShapleyValue.class);
	
	
	public ShapleyValue(CharacteristicFunction cfunction) {
		this.cfunction = cfunction;
		size = cfunction.getNbPlayers();
		currentRange = 0;
		factorialSize = FactorialUtil.factorial(size);

		this.output = new ArrayList<>();
		for (int i = 0; i <= size; i++) {
			output.add(0.0);
		}
	}
	
	public void calculate() {
		calculate(0,false);
	}

	public void calculate(long sampleSize, boolean randomValue) {
		if (logger.isDebugEnabled())
			logger.debug("ShapleyValue calculate started");


		if (permutations == null) {
			permutations = new PermutationLinkList(size);
		}

		int count = 1;
		if (sampleSize <= 0) {
			sampleSize = factorialSize;
		}

		while (!isLastReached() && count <= sampleSize) {
			List<Integer> coalition = null;
			if(!randomValue) { 
				coalition = permutations.getNextPermutation();
			} else  {
				coalition = RandomPermutations.getRandom(size);
			}
				
			currentRange++;
			// System.out.println("currentRange "+currentRange);
			count++;
			Set<Integer> set = new HashSet<>();
			double prevVal = 0;
			for (Integer element : coalition) {
				set.add(element);
				double val = cfunction.getValue(set) - prevVal;
				output.set(element, val + output.get(element));
				prevVal += val;
			}

		}

	}
	
	public Map<Integer, Double> getResult() {
		return getResult(0) ;
	}
	
	public Map<Integer, Double> getResult(int normalizedValue) {
		
		Map<Integer, Double> res = new HashMap<>();
		double total = 0;
		for (int i = 1; i <= size; i++) {
			total += output.get(i) / factorialSize;
			res.put(i, output.get(i) / factorialSize);
		}

		if (normalizedValue!=0) {
			Map<Integer, Double> normalizedRes = new HashMap<>();
			for (int i = 1; i <= size; i++) {
				normalizedRes.put(i, res.get(i) / total);
			}
			if (logger.isDebugEnabled())
				logger.debug("ShapleyValue calculate normalizedOutput={}", normalizedRes);
			return normalizedRes;
		}
		if (logger.isDebugEnabled())
			logger.debug("ShapleyValue getResult output={}", output);
		return res;
	}
	
	

	public boolean isLastReached() {
		if (currentRange < FactorialUtil.factorial(size))
			return false;
		else
			return true;
	}

	
	
	public List<Double> calculate(boolean normalized) {
		if(logger.isDebugEnabled()) logger.debug("ShapleyValue calculate started");
		
		long size = cfunction.getNbPlayers();
		long factorielSize = FactorialUtil.factorial(size);
		List<List<Integer>> permutations = Permutations.getAllPermutation(size);
		
		for(int i=0; i<=size; i++) {
			output.set(i, 0.0);
		}
		
		for(List<Integer> coalition : permutations) {
			Set<Integer> set = new HashSet<>();
			double prevVal =0;
			for(Integer element : coalition) {
				set.add(element);
				double val = cfunction.getValue(set)-prevVal;
				output.set(element,val+output.get(element));
				prevVal += val;
			}
		}
		
		double total = 0;
		for(int i=1; i<=size; i++) {
			total += output.get(i)/factorielSize;
			output.set(i, output.get(i)/factorielSize);
		}
		
		if(normalized) {
			for(int i=1; i<=size; i++) {
				output.set(i, output.get(i)/total);
			}
		}
		
		
		if(logger.isDebugEnabled()) logger.debug("ShapleyValue calculate output={}",output);
		return output;
		
	}
	
}
