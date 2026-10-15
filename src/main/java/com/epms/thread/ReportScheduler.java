package com.epms.thread;
import java.util.concurrent.*;
import java.util.logging.Logger;
/** Scheduled background worker for report cache refreshes. */
public class ReportScheduler implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ReportScheduler.class.getName());
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final SummaryCache cache;
    public ReportScheduler(SummaryCache cache) { this.cache = cache; }
    public void start() { executor.scheduleAtFixedRate(() -> { cache.refresh(java.util.Map.of()); LOGGER.fine("Summary cache refreshed"); }, 0, 5, TimeUnit.MINUTES); }
    @Override public void close() { executor.shutdownNow(); }
}
