package com.intrbiz.gerald.source;

import com.codahale.metrics.MetricRegistry;

public class DefaultIntelligenceSource extends AbstractIntelligenceSource
{
    public DefaultIntelligenceSource(String name)
    {
        super(name);
    }
    
    protected void register(MetricRegistry registry)
    {
    }
}
