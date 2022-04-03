package br.ufsc.egc.agrovoc.factory;

import br.ufsc.egc.agrovoc.service.AgrovocService;

import java.io.IOException;

public interface AgrovocServiceFactory {

    AgrovocService buildFromProperties() throws IOException;

    AgrovocService build(String tdbSchemaFolder, String thesaurusFile) throws IOException;

}
