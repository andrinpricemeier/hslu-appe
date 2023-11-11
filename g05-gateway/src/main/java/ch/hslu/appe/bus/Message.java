package ch.hslu.appe.bus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a message sent across the bus.
 * Its main purpose is encapsulate the span used to trace requests.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Message {
    private String spanContext;
    private String correlationId;
    private String replyTo;

    /**
     * Get the tracing information used by Jaeger.
     * @return the span context.
     */
    public String getSpanContext() {
        return spanContext;
    }
    /**
     * Set the tracing information used by Jaeger.
     * @param spanContext the span context.
     */
    public void setSpanContext(String spanContext) {
        this.spanContext = spanContext;
    }

    /**
     * Get the correlation id.
     * @return the correlation id.
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Set the correlation id.
     * @param correlationId the correlation id.
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * Get the reply to route.
     * @return the reply to route.
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Set the reply to route.
     * @param replyTo the reply to route.
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }  
    
}