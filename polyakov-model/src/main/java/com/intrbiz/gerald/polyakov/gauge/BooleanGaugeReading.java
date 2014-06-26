package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("boolean-gauge")
public class BooleanGaugeReading extends Reading implements GaugeReading<Boolean>
{
    @JsonProperty("value")
    private Boolean value;
    
    public BooleanGaugeReading()
    {
        super();
    }
    
    public BooleanGaugeReading(String name, Gauge<Boolean> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Boolean getValue()
    {
        return value;
    }

    @Override
    public void setValue(Boolean value)
    {
        this.value = value;
    }
}
