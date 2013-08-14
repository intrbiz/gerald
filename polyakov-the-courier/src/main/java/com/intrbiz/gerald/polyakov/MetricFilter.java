package com.intrbiz.gerald.polyakov;

import com.yammer.metrics.core.MetricName;

public interface MetricFilter
{
    /**
     * Does this filter match the given MetricName
     * @param name
     * @return
     */
    boolean match(MetricName name);
    
    /**
     * Should Polyakov reveal the matched metrics?
     */
    boolean reveal();
}
