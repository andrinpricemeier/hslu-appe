package ch.hslu.appe.bus;

import io.opentracing.Span;

/**
 * Represents a trace through the system, tracking requests.
 */
public interface Trace {
    /**
     * Creates a span based on the given span context or a new span if no context exists.
     * @param spanContext the context.
     * @param operationName the current operation's name.
     * @return the span.
     */
    Span createSpan(final String spanContext, final String operationName);

    /**
     * Extracts and transforms the context of the given span to a string so that it can be passed
     * to other services.
     * @param span the span.
     * @return the span context as a string.
     */
    String extractContext(final Span span);
}
