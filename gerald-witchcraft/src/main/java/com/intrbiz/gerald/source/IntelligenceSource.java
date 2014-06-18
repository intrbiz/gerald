package com.intrbiz.gerald.source;

import com.codahale.metrics.MetricRegistry;

/**
 * A source of intelligence which should be exposed by Gerald.
 * 
 * An intelligence source maintains a registry of Metrics, which 
 * capture performance information about your application.
 * 
 */
public interface IntelligenceSource
{
    /**
     * The name of the source 
     */
    String getName();

    /**
     * The registry to which metrics for this source are registered
     */
    MetricRegistry getRegistry();
    
    /**
     * Get any annotations for the given metric
     */
    MetricAnnotations annotations(String metricName);

    /**
     * Setup and start anything required to activate this source
     */
    void start();

    /**
     * Shutdown any required activities for this source
     */
    void shutdown();
}
