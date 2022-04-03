package br.ufsc.egc.agrovoc.conf;

public enum ServiceProperty {

    THESAURUS_FILE("file.thesaurus"),
    TDB_SCHEMA_FOLDER("tdb.schema.path");

    final String propertyName;

    ServiceProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
