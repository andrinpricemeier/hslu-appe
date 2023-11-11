package ch.hslu.appe.messages;

/**
 * Represents a generic message sent across the bus.
 */

public abstract class Message {
    private String spanContext;
    private String correlationId;
    private String replyTo;


    /**
     * Get the tracing information used by Jaeger.
     * @return the span context.
     */
    public String getSpanContext(){
        return spanContext;
    }

    /**
     * Set the tracing information used by Jaeger.
     * @param spanContext the span context.
     */
    public void setSpanContext(String spanContext){
        this.spanContext = spanContext;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

}
