package ch.hslu.appe.monitoring;

public interface Monitoring {
    void logEvent(final String businessId, final String businessType, final String businessSubtype, final String businessDescription);
}
