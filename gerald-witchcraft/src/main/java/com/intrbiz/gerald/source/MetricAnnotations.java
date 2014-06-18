package com.intrbiz.gerald.source;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Arbitrary annotations for a given metric
 */
public class MetricAnnotations
{
    private final ConcurrentMap<String, String> annotations = new ConcurrentHashMap<String, String>();
    
    public MetricAnnotations()
    {
        super();
    }
    
    public String get(String name)
    {
        return this.annotations.get(name);
    }
    
    public MetricAnnotations set(String name, String value)
    {
        this.annotations.put(name, value);
        return this;
    }
    
    public Set<Entry<String, String>> entries()
    {
        return this.annotations.entrySet();
    }
    
    public Set<String> names()
    {
        return this.annotations.keySet();
    }
    
    public MetricAnnotations remove(String name)
    {
        this.annotations.remove(name);
        return this;
    }
    
    public MetricAnnotations clear()
    {
        this.annotations.clear();
        return this;
    }
}
