package com.intrbiz.gerald.polyakov.filter;

import com.intrbiz.gerald.polyakov.MetricFilter;
import com.yammer.metrics.core.MetricName;

public class ClassFilter implements MetricFilter
{
    private Class<?> type;
    
    private boolean reveal;
    
    public ClassFilter(Class<?> type, boolean reveal)
    {
        super();
        this.reveal = reveal;
    }

    @Override
    public boolean match(MetricName name)
    {
        return type.getPackage().getName().equals(name.getGroup()) && type.getSimpleName().equals(name.getType());
    }

    @Override
    public boolean reveal()
    {
        return this.reveal;
    }
}
