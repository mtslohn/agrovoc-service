package br.ufsc.egc.agrovoc.util.tdb;

import br.ufsc.egc.agrovoc.conf.PropertyLoader;
import br.ufsc.egc.agrovoc.conf.ServiceProperty;
import br.ufsc.egc.util.tdb.TDBCreator;
import br.ufsc.egc.util.tdb.TDBCreator.SourceFile;

import java.io.IOException;

public class AgrovocTDBCreatorApplication {

    public static void main(String[] args) throws IOException {
        PropertyLoader props = new PropertyLoader();

        new TDBCreator().createTDBSchema(
                props.getProperty(ServiceProperty.TDB_SCHEMA_FOLDER),
                new SourceFile(props.getProperty(ServiceProperty.THESAURUS_FILE), "RDF/XML")
        ).close();
    }

}
