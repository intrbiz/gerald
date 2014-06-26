package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("float-gauge")
public class FloatGaugeReading extends Reading implements GaugeReading<Float>
{
    @JsonProperty("value")
    private Float value;
    
    public FloatGaugeReading()
    {
        super();
    }
    
    public FloatGaugeReading(String name, Gauge<Float> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Float getValue()
    {
        return value;
    }

    @Override
    public void setValue(Float value)
    {
        this.value = value;
    }
}
