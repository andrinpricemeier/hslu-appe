package ch.hslu.appe.bus;

import io.opentracing.propagation.TextMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapTextMap implements TextMap {
    protected final Map<String,String> map;

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
