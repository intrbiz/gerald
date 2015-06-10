package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.ContinuousGaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("float-gauge")
public class FloatGaugeReading extends Reading implements ContinuousGaugeReading<Float>
{
    @JsonProperty("value")
    private Float value;
    
    @JsonProperty("warning")
    private Float warning;
    
    @JsonProperty("critical")
    private Float critical;
    
    @JsonProperty("min")
    private Float min;
    
    @JsonProperty("max")
    private Float max;
    
    public FloatGaugeReading()
    {
        super();
    }
    
    public FloatGaugeReading(String name, Gauge<Float> metric)
    {
        super(name);
        this.value = metric.getValue();
    }
    
    public FloatGaugeReading(String name, String unit, Float value)
    {
        super(name, unit);
        this.value = value;
    }
    
    public FloatGaugeReading(String name, String unit, Float value, Float warning, Float critical, Float min, Float max)
    {
        super(name, unit);
        this.value = value;
        this.warning = warning;
        this.critical = critical;
        this.min = min;
        this.max = max;
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
    
    @Override
    public Float getWarning()
    {
        return warning;
    }

    @Override
    public void setWarning(Float warning)
    {
        this.warning = warning;
    }

    @Override
    public Float getCritical()
    {
        return critical;
    }

    @Override
    public void setCritical(Float critical)
    {
        this.critical = critical;
    }

    @Override
    public Float getMin()
    {
        return min;
    }

    @Override
    public void setMin(Float min)
    {
        this.min = min;
    }

    @Override
    public Float getMax()
    {
        return max;
    }

    @Override
    public void setMax(Float max)
    {
        this.max = max;
    }
}
