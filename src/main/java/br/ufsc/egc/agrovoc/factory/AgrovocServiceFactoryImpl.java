package br.ufsc.egc.agrovoc.factory;

import br.ufsc.egc.agrovoc.conf.PropertyLoader;
import br.ufsc.egc.agrovoc.conf.ServiceProperty;
import br.ufsc.egc.agrovoc.service.AgrovocService;
import br.ufsc.egc.agrovoc.service.AgrovocServiceImpl;

import java.io.IOException;

public class AgrovocServiceFactoryImpl implements AgrovocServiceFactory {

    @Override
    public AgrovocService buildFromProperties() throws IOException {
        PropertyLoader propertyLoader = new PropertyLoader();
        return build(propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_FOLDER),
                propertyLoader.getProperty(ServiceProperty.THESAURUS_FILE));
    }

    @Override
    public AgrovocService build(String tdbSchemaFolder, String thesaurusFile) throws IOException {
        return new AgrovocServiceImpl(tdbSchemaFolder, thesaurusFile);
    }
}
