package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.ContinuousGaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("integer-gauge")
public class IntegerGaugeReading extends Reading implements ContinuousGaugeReading<Integer>
{
    @JsonProperty("value")
    private Integer value;
    
    @JsonProperty("warning")
    private Integer warning;
    
    @JsonProperty("critical")
    private Integer critical;
    
    @JsonProperty("min")
    private Integer min;
    
    @JsonProperty("max")
    private Integer max;
    
    public IntegerGaugeReading()
    {
        super();
    }
    
    public IntegerGaugeReading(String name, Gauge<Integer> metric)
    {
        super(name);
        this.value = metric.getValue();
    }
    
    public IntegerGaugeReading(String name, String unit, Integer value)
    {
        super(name, unit);
        this.value = value;
    }
    
    public IntegerGaugeReading(String name, String unit, Integer value, Integer warning, Integer critical, Integer min, Integer max)
    {
        super(name, unit);
        this.value = value;
        this.warning = warning;
        this.critical = critical;
        this.min = min;
        this.max = max;
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
    
    @Override
    public Integer getWarning()
    {
        return warning;
    }

    @Override
    public void setWarning(Integer warning)
    {
        this.warning = warning;
    }

    @Override
    public Integer getCritical()
    {
        return critical;
    }

    @Override
    public void setCritical(Integer critical)
    {
        this.critical = critical;
    }

    @Override
    public Integer getMin()
    {
        return min;
    }

    @Override
    public void setMin(Integer min)
    {
        this.min = min;
    }

    @Override
    public Integer getMax()
    {
        return max;
    }

    @Override
    public void setMax(Integer max)
    {
        this.max = max;
    }
}
