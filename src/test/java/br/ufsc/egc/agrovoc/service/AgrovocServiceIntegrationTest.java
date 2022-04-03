package br.ufsc.egc.agrovoc.service;

import br.ufsc.egc.agrovoc.factory.AgrovocServiceFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AgrovocServiceIntegrationTest {

	AgrovocService service;

	@Before
	public void prepare() throws IOException {
		service = new AgrovocServiceFactoryImpl().buildFromProperties();
	}

	@Test
	public void verifyIfExistsLabel_when_mustBePresent() {
		assertThat(service.verifyIfExistsLabel("Agricultura"), is(true));
	}

	@Test
	public void verifyIfExistsLabel_when_mustBeAbsent() {
		assertThat(service.verifyIfExistsLabel("Xuxu"), is(false));
	}

	@Test
	public void isParent_when_elementIsParent() {
		assertThat(service.isParent("Milho", "Produto"), is(true));
	}

}
