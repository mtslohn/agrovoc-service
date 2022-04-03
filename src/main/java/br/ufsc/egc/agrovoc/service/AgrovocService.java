package br.ufsc.egc.agrovoc.service;

public interface AgrovocService {
    boolean verifyIfExistsLabel(String label);

    boolean isParent(String label, String parent);
}
