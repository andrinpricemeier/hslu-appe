package ch.hslu.appe.bus;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;

import java.util.HashMap;

public final class JaegerTrace implements Trace {
    //private static final Tracer tracer = Configuration.fromEnv("customer").getTracer(); //local environment
    private static final Tracer tracer = Configuration.fromEnv().getTracer(); //production environment

    @Override
    public Span createSpan(final String spanContext, final String operationName) {
        Span span = null;
        if (spanContext != null && !spanContext.equals("")) {
            final HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("uber-trace-id", spanContext);
            SpanContext parentSpanCtx = tracer.extract(io.opentracing.propagation.Format.Builtin.TEXT_MAP,
                    new HashMapTextMap(headers));
            span = tracer.buildSpan(operationName).asChildOf(parentSpanCtx).start();
        } else {
            span = tracer.buildSpan(operationName).start();
        }
        return span;
    }

    @Override
    public String extractContext(Span span) {
        return TextMapCodec.contextAsString((JaegerSpanContext) span.context());
    }
}
