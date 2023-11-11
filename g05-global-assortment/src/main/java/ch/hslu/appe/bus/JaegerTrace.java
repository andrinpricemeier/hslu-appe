package ch.hslu.appe.bus;

import java.util.HashMap;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;

/**
 * Represents a Jaeger implementation of an opentracing trace.
 */
public final class JaegerTrace implements Trace {
    private static final Tracer tracer = Configuration.fromEnv().getTracer();

    @Override
    public Span createChildOf(final String spanContext, final String operationName) {
        if (spanContext != null && !spanContext.equals("")) {
            return tracer.buildSpan(operationName).asChildOf(createParentSpanContext(spanContext)).start();
        } else {
            return tracer.buildSpan(operationName).start();
        }
    }

    @Override
    public Span createFollowsFrom(String spanContext, String operationName) {
        if (spanContext != null && !spanContext.equals("")) {
            return tracer.buildSpan(operationName)
                    .addReference(References.FOLLOWS_FROM, createParentSpanContext(spanContext)).start();
        } else {
            return tracer.buildSpan(operationName).start();
        }
    }

    @Override
    public String extractContext(Span span) {
        return TextMapCodec.contextAsString((JaegerSpanContext) span.context());
    }

    private SpanContext createParentSpanContext(final String spanContext) {
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("uber-trace-id", spanContext);
        return tracer.extract(io.opentracing.propagation.Format.Builtin.TEXT_MAP, new HashMapTextMap(headers));
    }
}