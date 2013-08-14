package com.intrbiz.gerald;

import com.yammer.metrics.core.MetricsRegistry;

public abstract class InteligenceSource
{
    protected final String name;

    protected final MetricsRegistry registry = new MetricsRegistry();

    protected InteligenceSource(String name)
    {
        super();
        this.name = name;
    }

    public final String getName()
    {
        return this.name;
    }

    public final MetricsRegistry getRegistry()
    {
        return this.registry;
    }

    public final MetricsRegistry register()
    {
        this.register(this.registry);
        return this.registry;
    }

    public final void start()
    {
        this.doStart();
    }

    public final void shutdown()
    {
        this.doShutdown();
    }

    protected abstract void register(MetricsRegistry registry);

    protected void doStart()
    {
    }

    protected void doShutdown()
    {
    }
}
