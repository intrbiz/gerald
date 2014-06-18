package com.intrbiz.gerald.polyakov;


public interface MetricFilter
{
    /**
     * Does this filter match the given MetricName
     * @param name
     * @return
     */
    boolean match(String name);
    
    /**
     * Should Polyakov reveal the matched metrics?
     */
    boolean reveal();
}
