package br.ufsc.egc.agrovoc.service.util.tdb;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

public class AgrovocTDBCreator {
	
	public static final String THESAURUS_FILE = "resources/agrovoc_2016-01-21_core.rdf";
	public static final String TDB_DIRECTORY = "resources/tdb/agrovoc/";
	
	public Model createTDB() {
		Model model = ModelFactory.createDefaultModel();
		model = TDBFactory.createDataset(TDB_DIRECTORY).getDefaultModel();
		InputStream thesaurusStream = FileManager.get().open(THESAURUS_FILE);
		model.read(thesaurusStream, null, "RDF/XML");
		return model;
	}
	
	public static void main(String[] args) {
		new AgrovocTDBCreator().createTDB().close();;
	}

}
