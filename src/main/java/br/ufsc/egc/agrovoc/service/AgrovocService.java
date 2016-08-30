package br.ufsc.egc.agrovoc.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.ResultSetStream;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.Logger;

import br.ufsc.egc.agrovoc.service.util.tdb.AgrovocTDBCreator;

public class AgrovocService {

	private static AgrovocService instance;

	private final Logger LOGGER = Logger.getLogger(getClass());

	private Model model;

	private AgrovocService() {
		LOGGER.info("Carregando serviço da DBPedia...");
		loadModel();
		LOGGER.info("Serviço da DBPedia carregado");
	}

	public static AgrovocService getInstance() {
		if (instance == null) {
			instance = new AgrovocService();
		}
		return instance;
	}

	private void loadModel() {
		File directory = new File(AgrovocTDBCreator.TDB_DIRECTORY);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		model = TDBFactory.createDataset(AgrovocTDBCreator.TDB_DIRECTORY).getDefaultModel();
		if (model.isEmpty()) {
			model = new AgrovocTDBCreator().createTDB();
		}
	}

	public boolean verifyIfExistsLabel(String label) {
		
		BasicPattern basicPattern = new BasicPattern();
		
		Var varConcept = Var.alloc("concept");
		final String SKOS_URI = "http://www.w3.org/2008/05/skos-xl#";
		
		basicPattern.add(new Triple(varConcept, NodeFactory.createURI(SKOS_URI + "literalForm"), NodeFactory.createLiteral(label, "pt")));
		
		Op op = new OpBGP(basicPattern);
		
		QueryIterator queryIterator = Algebra.exec(op, model);
		
		List<String> resultVars = new ArrayList<String>();
		resultVars.add("concept");
		ResultSet rs = new ResultSetStream(resultVars , model, queryIterator);
		
		if (rs.hasNext()) {
			return true;
		}
		
		return false;

	}

}
