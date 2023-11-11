package ch.hslu.appe.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LokiMonitoring implements Monitoring {
    private static final Logger LOG = LoggerFactory.getLogger(LokiMonitoring.class);

    @Override
    public void logEvent(String businessId, String businessType, String businessSubtype, String businessDescription) {
        final var businessEvent = new BusinessEvent();
        businessEvent.setBusinessId(businessId);
        businessEvent.setBusinessType(businessType);
        businessEvent.setBusinessSubtype(businessSubtype);
        businessEvent.setBusinessDescription(businessDescription);
        LOG.info("\"\" " + businessEvent.toString());
    }
}
