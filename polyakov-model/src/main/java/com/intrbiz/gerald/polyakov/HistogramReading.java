package com.intrbiz.gerald.polyakov;

import com.codahale.metrics.Histogram;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("histogram")
public class HistogramReading extends Reading implements CountingReading, SamplingReading
{
    @JsonProperty("count")
    private long count;
    
    @JsonProperty("snapshot")
    private ReadingSnapshot snapshot;
    
    public HistogramReading()
    {
        super();
    }
    
    public HistogramReading(String name, Histogram metric)
    {
        super(name);
        this.count = metric.getCount();
        this.snapshot = new ReadingSnapshot(metric.getSnapshot());
    }

    @Override
    public long getCount()
    {
        return count;
    }

    @Override
    public void setCount(long count)
    {
        this.count = count;
    }
    
    @Override
    public ReadingSnapshot getSnapshot()
    {
        return snapshot;
    }

    @Override
    public void setSnapshot(ReadingSnapshot snapshot)
    {
        this.snapshot = snapshot;
    }
}
