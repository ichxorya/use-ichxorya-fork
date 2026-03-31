package org.tzi.use.psum.model;

/**
 * PSUM v1.0 §12.1
 * Represents a characteristic of a Measurement (e.g. Accuracy, Precision, Resolution).
 */
public class MeasurableFeature extends PSUMElement {
    
    private String featureName; // e.g., "Accuracy", "Precision"
    private double featureValue;
    private MeasurableElement owner;

    public MeasurableFeature(String id, String featureName, double featureValue) {
        super(id);
        this.featureName = featureName;
        this.featureValue = featureValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public double getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(double featureValue) {
        this.featureValue = featureValue;
    }

    public MeasurableElement getOwner() {
        return owner;
    }

    public void setOwner(MeasurableElement owner) {
        this.owner = owner;
    }
}
