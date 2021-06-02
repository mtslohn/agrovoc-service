package br.ufsc.egc.agrovoc.service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Ignore;

/**
 * Unit test for simple App.
 */
@Ignore(value = "It depends on mocking index resources instead of using the full index")
public class AgrovocServiceTest extends TestCase {

	public AgrovocServiceTest(String testName) {
		super(testName);
	}
	public static Test suite() {
		return new TestSuite(AgrovocServiceTest.class);
	}

	public void testTrue() {
		AgrovocService service = AgrovocService.getInstance();
		boolean response = service.verifyIfExistsLabel("Agricultura");
		assertTrue(response);
	}

	public void testFalse() {
		AgrovocService service = AgrovocService.getInstance();
		boolean response = service.verifyIfExistsLabel("Xuxu");
		assertFalse(response);
	}

	public void testParent() {
		AgrovocService service = AgrovocService.getInstance();
		boolean response = service.isParent("Milho", "Produto");
		assertTrue(response);
	}
	
}
