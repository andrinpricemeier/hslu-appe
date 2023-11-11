package ch.hslu.appe.bus;

import io.opentracing.Span;

/**
 * Represents a trace through the system, tracking requests.
 */
public interface Trace {
    /**
     * Creates a child span based on the given parent span's context.
     * Use this if you are in strict parent-child relationship where the parent has to wait for the child to finish.
     * @param spanContext the context.
     * @param operationName the current operation's name.
     * @return the span.
     */
    Span createChildOf(final String spanContext, final String operationName);

    /**
     * Creates a span that references another span.
     * This can be used in scenarios where messages are sent asynchronously across the bus but are still related to eachother somehow.
     * @param spanContext the context.
     * @param operationName the current operation's name.
     * @return the span.
     */
    Span createFollowsFrom(final String spanContext, final String operationName);

    /**
     * Extracts and transforms the context of the given span to a string so that it can be passed
     * to other services.
     * @param span the span.
     * @return the span context as a string.
     */
    String extractContext(final Span span);
}