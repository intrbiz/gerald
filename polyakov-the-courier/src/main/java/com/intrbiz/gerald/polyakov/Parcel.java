package com.intrbiz.gerald.polyakov;

import java.util.HashMap;
import java.util.Map;

import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;


public class Parcel
{
    private Node node;
    
    private Map<MetricName,Metric> metrics = new HashMap<MetricName,Metric>();
    
    public Parcel()
    {
        super();
    }
    
    public Parcel(Node node)
    {
        this();
        this.node = node;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Map<MetricName, Metric> getMetrics()
    {
        return metrics;
    }

    public void setMetrics(Map<MetricName, Metric> metrics)
    {
        this.metrics = metrics;
    }
    
    public void addMetric(MetricName name, Metric metric)
    {
        this.metrics.put(name, metric);
    }
}
