package br.ufsc.egc.agrovoc.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.ResultSetStream;
import org.apache.jena.util.FileManager;
import org.apache.log4j.Logger;

public class AgrovocService {

	private static final String THESAURUS_FILE = "resources/agrovoc_2016-01-21_core.rdf";

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
		model = ModelFactory.createDefaultModel();
		InputStream thesaurusStream = FileManager.get().open(THESAURUS_FILE);
		model.read(thesaurusStream, null, "RDF/XML");
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
