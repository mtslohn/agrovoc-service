package br.ufsc.egc.agrovoc.service;

import br.ufsc.egc.util.tdb.TDBCreator;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.ResultSetStream;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgrovocServiceImpl implements AgrovocService {

	private static final Logger LOGGER = Logger.getLogger(AgrovocServiceImpl.class.getName());
	private Model model;

	public AgrovocServiceImpl(String tdbSchemaFolder, String thesaurusFile) throws IOException {
		LOGGER.info("Carregando serviço da DBPedia...");
		loadModel(tdbSchemaFolder, thesaurusFile);
		LOGGER.info("Serviço da DBPedia carregado");
	}

	private void loadModel(String tdbSchemaFolder, String thesaurusFile) throws IOException {
		createFolderIfNotExists(tdbSchemaFolder);
		model = TDBFactory.createDataset(tdbSchemaFolder).getDefaultModel();
		if (model.isEmpty()) {
			model = new TDBCreator().createTDBSchema(tdbSchemaFolder,
					new TDBCreator.SourceFile(thesaurusFile, "RDF/XML"));
		}
	}

	private void createFolderIfNotExists(String tdbSchemaFolder) {
		File directory = new File(tdbSchemaFolder);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	@Override
	public boolean verifyIfExistsLabel(String label) {

		BasicPattern basicPattern = new BasicPattern();

		Var varConcept = Var.alloc("concept");
		final String SKOS_URI = "http://www.w3.org/2008/05/skos-xl#";

		basicPattern.add(new Triple(varConcept,
				NodeFactory.createURI(SKOS_URI + "literalForm"),
				NodeFactory.createLiteral(label, "pt")));

		Op op = new OpBGP(basicPattern);

		QueryIterator queryIterator = Algebra.exec(op, model);

		List<String> resultVars = new ArrayList<>();
		resultVars.add("concept");
		ResultSet rs = new ResultSetStream(resultVars, model, queryIterator);

		return rs.hasNext();
	}

	@Override
	public boolean isParent(String label, String parent) {

		ElementPathBlock pattern = new ElementPathBlock();

		Var varConceptLabel = Var.alloc("conceptLabel");
		Var varConcept = Var.alloc("concept");
		Var varBroader = Var.alloc("broader");
		Var varBroaderLabel = Var.alloc("broaderLabel");
		Var varLiteralForm = Var.alloc("literalForm");

		final String SKOS_URI = "http://www.w3.org/2008/05/skos-xl#";
		final String SKOS_CORE_URI = "http://www.w3.org/2004/02/skos/core#";

		pattern.addTriple(new Triple(varConceptLabel, NodeFactory.createURI(SKOS_URI + "literalForm"), NodeFactory.createLiteral(label, "pt")));
		pattern.addTriple(new Triple(varConcept, NodeFactory.createURI(SKOS_URI + "prefLabel"), varConceptLabel));
		pattern.addTriplePath(new TriplePath(varConcept, PathFactory.pathZeroOrMoreN(PathFactory.pathLink(NodeFactory.createURI(SKOS_CORE_URI + "broader"))), varBroader));
		pattern.addTriple(new Triple(varBroader, NodeFactory.createURI(SKOS_URI + "prefLabel"), varBroaderLabel));
		pattern.addTriple(new Triple(varBroaderLabel, NodeFactory.createURI(SKOS_URI + "literalForm"), varLiteralForm));

		Op op = Algebra.compile(pattern);
		QueryIterator queryIterator = Algebra.exec(op, model);

		List<String> resultVars = new ArrayList<>();
		resultVars.add("literalForm");
		ResultSet rs = new ResultSetStream(resultVars, model, queryIterator);

		while (rs.hasNext()) {
			Literal literal = rs.nextSolution().getLiteral("literalForm");
			if (literal.getLanguage().equals("pt")) {
				if (parent.equalsIgnoreCase(literal.getString())) {
					return true;
				}
			}
		}

		return false;

	}

}
