package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("double-gauge")
public class DoubleGaugeReading extends Reading implements GaugeReading<Double>
{
    @JsonProperty("value")
    private Double value;
    
    public DoubleGaugeReading()
    {
        super();
    }
    
    public DoubleGaugeReading(String name, Gauge<Double> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Double getValue()
    {
        return value;
    }

    @Override
    public void setValue(Double value)
    {
        this.value = value;
    }
}
