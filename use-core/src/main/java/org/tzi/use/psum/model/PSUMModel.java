package org.tzi.use.psum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PSUM v1.0 §9.3
 * Contains all PSUM Elements matching to exactly one USE Specification.
 */
public class PSUMModel {
    private String modelName;
    private List<PSUMElement> elements;

    public PSUMModel(String modelName) {
        this.modelName = modelName;
        this.elements = new ArrayList<>();
    }

    public String getModelName() {
        return modelName;
    }

    public void addElement(PSUMElement element) {
        this.elements.add(element);
    }

    public List<PSUMElement> getElements() {
        return elements;
    }
    
    public <T extends PSUMElement> List<T> getElementsByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (PSUMElement e : elements) {
            if (type.isInstance(e)) {
                result.add(type.cast(e));
            }
        }
        return result;
    }
}
