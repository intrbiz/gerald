package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("long-gauge")
public class LongGaugeReading extends Reading implements GaugeReading<Long>
{
    @JsonProperty("value")
    private Long value;
    
    public LongGaugeReading()
    {
        super();
    }
    
    public LongGaugeReading(String name, Gauge<Long> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Long getValue()
    {
        return value;
    }

    @Override
    public void setValue(Long value)
    {
        this.value = value;
    }
}
