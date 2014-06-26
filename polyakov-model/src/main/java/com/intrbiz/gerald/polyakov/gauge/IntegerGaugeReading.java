package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("integer-gauge")
public class IntegerGaugeReading extends Reading implements GaugeReading<Integer>
{
    @JsonProperty("value")
    private Integer value;
    
    public IntegerGaugeReading()
    {
        super();
    }
    
    public IntegerGaugeReading(String name, Gauge<Integer> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Integer getValue()
    {
        return value;
    }

    @Override
    public void setValue(Integer value)
    {
        this.value = value;
    }
}
