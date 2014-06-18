package com.intrbiz.gerald.polyakov.filter;

import com.intrbiz.gerald.polyakov.MetricFilter;

public class StartsWithFilter implements MetricFilter
{
    private String prefix;
    
    private boolean reveal;
    
    public StartsWithFilter(String prefix, boolean reveal)
    {
        super();
        this.prefix = prefix;
        this.reveal = reveal;
    }

    @Override
    public boolean match(String name)
    {
        return name.startsWith(this.prefix);
    }

    @Override
    public boolean reveal()
    {
        return this.reveal;
    }
}
