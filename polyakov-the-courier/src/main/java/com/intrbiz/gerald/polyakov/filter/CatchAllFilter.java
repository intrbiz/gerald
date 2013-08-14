package com.intrbiz.gerald.polyakov.filter;

import com.intrbiz.gerald.polyakov.MetricFilter;
import com.yammer.metrics.core.MetricName;

public class CatchAllFilter implements MetricFilter
{
    private boolean reveal;
    
    public CatchAllFilter(boolean reveal)
    {
        super();
        this.reveal = reveal;
    }

    @Override
    public boolean match(MetricName name)
    {
        return true;
    }

    @Override
    public boolean reveal()
    {
        return this.reveal;
    }
}
