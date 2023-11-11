package ch.hslu.appe.bus;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import io.opentracing.propagation.TextMap;

/**
 * Used to create a span based on the context sent across the RabbitMQ bus.
 */
public class HashMapTextMap implements TextMap {
    protected final Map<String,String> map;

    /**
     * Creates a new instance.
     * @param map the underlying map.
     */
    public HashMapTextMap(final Map<String,String> map) {
        this.map = map;
    }

    @Override
    public void put(String key, String value) {
        map.put(key, value);
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return map.entrySet().iterator();
    }
}
