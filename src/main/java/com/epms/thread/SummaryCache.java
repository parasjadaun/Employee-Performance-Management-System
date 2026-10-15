package com.epms.thread;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/** Thread-safe cache for periodically refreshed report summaries. */
public class SummaryCache {
    private final Map<String, Double> values = new ConcurrentHashMap<>();
    private final AtomicInteger refreshCount = new AtomicInteger();
    public synchronized void refresh(Map<String, Double> freshValues) { values.clear(); values.putAll(freshValues); refreshCount.incrementAndGet(); }
    public Map<String, Double> snapshot() { return Map.copyOf(values); }
    public int getRefreshCount() { return refreshCount.get(); }
}
