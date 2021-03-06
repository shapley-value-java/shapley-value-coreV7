package org.shapleyvalue.application.impl.fraud;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.shapleyvalue.application.facade.CoalitionStrategy;
import org.shapleyvalue.application.facade.ShapleyApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FraudRuleApplicationTest {
	
	private final Logger logger = LoggerFactory.getLogger(FraudRuleApplicationTest.class);

	@Test
	public void testEvaluationOneRule() {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1)
				.build();
	
		
		Map<String,Double> output = evaluation.calculate();
		double phiRule1 = output.get("Rule1");
		
		assertEquals(phiRule1, 1.0, 0.01);
	}
	

	@Test
	public void testEvaluationTwoRules() {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1)
				.addRule("Rule2", 1)
				.build();
	
		
		Map<String,Double> output = evaluation.calculate();
		double phiRule1 = output.get("Rule1");
		double phiRule2 = output.get("Rule2");
		
		assertEquals(phiRule1, 0.5, 0.01);
		assertEquals(phiRule2, 0.5, 0.01);
	}
	
	@Test
	public void testEvaluationFourRules() {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3)
				.addRule("Rule2", 1,2,3)
				.addRule("Rule3", 1,2,3)
				.addRule("Rule4", 4)
				.build();
		
		Map<String,Double> output = evaluation.calculate();
		double phiRule1 = output.get("Rule1");
		double phiRule2 = output.get("Rule2");
		double phiRule3 = output.get("Rule3");
		double phiRule4 = output.get("Rule4");
		
		assertEquals(phiRule1, 0.25, 0.01);
		assertEquals(phiRule2, 0.25, 0.01);
		assertEquals(phiRule3, 0.25, 0.01);
		assertEquals(phiRule4, 0.25, 0.01);
	}
	
	@Test
	public void testEvaluationFourRulesPerStep() throws ShapleyApplicationException {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3)
				.addRule("Rule2", 1,2,3)
				.addRule("Rule3", 1,2,3)
				.addRule("Rule4", 4)
				.build();
		
		Map<String,Double> output =null;
		while(!evaluation.isLastCoalitionReached()) {
			output = evaluation.calculate(1);
		}
		double phiRule1 = output.get("Rule1");
		double phiRule2 = output.get("Rule2");
		double phiRule3 = output.get("Rule3");
		double phiRule4 = output.get("Rule4");
		
		assertEquals(phiRule1, 0.25, 0.01);
		assertEquals(phiRule2, 0.25, 0.01);
		assertEquals(phiRule3, 0.25, 0.01);
		assertEquals(phiRule4, 0.25, 0.01);
	}
	
	
	@Test
	public void testEvaluationXXXRules() throws ShapleyApplicationException {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3,4,5,7,9)
				.addRule("Rule2", 1,3,6,8)
				.addRule("Rule3", 1,2,7)
				.addRule("Rule4", 1,9)
				.addRule("Rule5", 2)
				.addRule("Rule6", 1,2)
				.addRule("Rule7", 3)
				.addRule("Rule8", 9)
				.addRule("Rule9", 1,8)
				.build();
	
		
		for(int i=1; i<10;i++) {
			Map<String,Double> output = evaluation.calculate(10_000,CoalitionStrategy.RANDOM);
			logger.info("OUTPUT {}",output);
			double phiRule1 = output.get("Rule1");
			double phiRule2 = output.get("Rule2");
			logger.info("loop {}",i);
			logger.info("phiRule1={}",String.format("%.3f", phiRule1));
			logger.info("phiRule2={}",String.format("%.3f", phiRule2));
		}

	}
	
	@Test
	public void testIsLastCoalitionReachedFalse() throws ShapleyApplicationException {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3)
				.addRule("Rule2", 1,2,3)
				.build();
	
		
		assertFalse(evaluation.isLastCoalitionReached());

	}
	
	@Test
	public void testIsLastCoalitionReachedTrue() throws ShapleyApplicationException {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3)
				.addRule("Rule2", 1,2,3)
				.build();
	
		
		assertFalse(evaluation.isLastCoalitionReached());
		evaluation.calculate(1);
		assertFalse(evaluation.isLastCoalitionReached());
		evaluation.calculate(1);
		assertTrue(evaluation.isLastCoalitionReached());
		

	}


}
