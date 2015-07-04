package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.GaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("string-gauge")
public class StringGaugeReading extends Reading implements GaugeReading<String>
{
    @JsonProperty("value")
    private String value;
    
    public StringGaugeReading()
    {
        super();
    }
    
    public StringGaugeReading(String name, Gauge<String> metric)
    {
        super(name);
        this.value = metric.getValue();
    }
    
    public StringGaugeReading(String name, String unit, String value)
    {
        super(name, unit);
        this.value = value;
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}
