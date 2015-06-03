package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.ContinuousGaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("double-gauge")
public class DoubleGaugeReading extends Reading implements ContinuousGaugeReading<Double>
{
    @JsonProperty("value")
    private Double value;
    
    @JsonProperty("warning")
    private Double warning;
    
    @JsonProperty("critical")
    private Double critical;
    
    @JsonProperty("min")
    private Double min;
    
    @JsonProperty("max")
    private Double max;
    
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

    @Override
    public Double getWarning()
    {
        return warning;
    }

    @Override
    public void setWarning(Double warning)
    {
        this.warning = warning;
    }

    @Override
    public Double getCritical()
    {
        return critical;
    }

    @Override
    public void setCritical(Double critical)
    {
        this.critical = critical;
    }

    @Override
    public Double getMin()
    {
        return min;
    }

    @Override
    public void setMin(Double min)
    {
        this.min = min;
    }

    @Override
    public Double getMax()
    {
        return max;
    }

    @Override
    public void setMax(Double max)
    {
        this.max = max;
    }
}
