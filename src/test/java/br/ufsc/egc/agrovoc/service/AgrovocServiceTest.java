package br.ufsc.egc.agrovoc.service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AgrovocServiceTest extends TestCase {

	public AgrovocServiceTest(String testName) {
		super(testName);
	}
	public static Test suite() {
		return new TestSuite(AgrovocServiceTest.class);
	}

	public void testApp() {
		AgrovocService service = AgrovocService.getInstance();
		boolean response = service.verifyIfExistsLabel("Agricultura");
		assertTrue(response);
	}
	
}
