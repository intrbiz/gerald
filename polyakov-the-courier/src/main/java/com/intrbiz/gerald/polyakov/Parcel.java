package com.intrbiz.gerald.polyakov;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Metric;

/**
 * A parcel of metrics from a source
 */
public class Parcel
{
    private Node node;

    private String source;

    private Map<String, Metric> metrics = new HashMap<String, Metric>();

    public Parcel()
    {
        super();
    }

    public Parcel(Node node, String source)
    {
        this();
        this.node = node;
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Map<String, Metric> getMetrics()
    {
        return metrics;
    }

    public void setMetrics(Map<String, Metric> metrics)
    {
        this.metrics = metrics;
    }

    public void addMetric(String name, Metric metric)
    {
        this.metrics.put(name, metric);
    }
}
