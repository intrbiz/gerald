package com.intrbiz.gerald.source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.Timer;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public abstract class AbstractIntelligenceSource implements IntelligenceSource
{
    protected final String name;

    protected final MetricRegistry registry = new MetricRegistry();

    protected final ConcurrentMap<String, MetricAnnotations> annotations = new ConcurrentHashMap<String, MetricAnnotations>();

    protected AbstractIntelligenceSource(String name)
    {
        super();
        this.name = name;
        this.registry.addListener(new MetricRegistryListener()
        {
            @Override
            public void onGaugeAdded(String name, Gauge<?> gauge)
            {
                annotations.put(name, new MetricAnnotations());
            }

            @Override
            public void onGaugeRemoved(String name)
            {
                annotations.remove(name);
            }

            @Override
            public void onCounterAdded(String name, Counter counter)
            {
                annotations.put(name, new MetricAnnotations());
            }

            @Override
            public void onCounterRemoved(String name)
            {
                annotations.remove(name);
            }

            @Override
            public void onHistogramAdded(String name, Histogram histogram)
            {
                annotations.put(name, new MetricAnnotations());
            }

            @Override
            public void onHistogramRemoved(String name)
            {
                annotations.remove(name);
            }

            @Override
            public void onMeterAdded(String name, Meter meter)
            {
                annotations.put(name, new MetricAnnotations());
            }

            @Override
            public void onMeterRemoved(String name)
            {
                annotations.remove(name);
            }

            @Override
            public void onTimerAdded(String name, Timer timer)
            {
                annotations.put(name, new MetricAnnotations());
            }

            @Override
            public void onTimerRemoved(String name)
            {
                annotations.remove(name);
            }
        });
    }

    @Override
    public final String getName()
    {
        return this.name;
    }

    @Override
    public final MetricRegistry getRegistry()
    {
        return this.registry;
    }

    @Override
    public MetricAnnotations annotations(String metricName)
    {
        return this.annotations.get(metricName);
    }

    @Override
    public void start()
    {
        this.register(this.registry);
    }

    protected abstract void register(MetricRegistry registry);
    
    /**
     * Create a scoped metric name within this source
     */
    public String scoped(String name, String scope)
    {
        return Witchcraft.name(this.getName(), name, "[" + scope + "]");
    }
    
    /**
     * Create a metric name within this source
     */
    public String name(String... names)
    {
        return Witchcraft.name(this.getName(), names);
    }

    @Override
    public void shutdown()
    {
    }
}
