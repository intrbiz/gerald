package com.intrbiz.gerald.polyakov;

import com.codahale.metrics.Counter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("counter")
public class CounterReading extends Reading implements CountingReading
{
    @JsonProperty("count")
    private long count;
    
    public CounterReading()
    {
        super();
    }
    
    public CounterReading(String name, Counter metric)
    {
        super(name);
        this.count = metric.getCount();
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
}
