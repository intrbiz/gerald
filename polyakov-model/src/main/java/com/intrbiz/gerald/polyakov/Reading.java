package com.intrbiz.gerald.polyakov;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
public abstract class Reading
{
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("unit")
    private String unit;
    
    public Reading()
    {
        super();
    }
    
    public Reading(String name)
    {
        super();
        this.name = name;
    }
    
    public Reading(String name, String unit)
    {
        this(name);
        this.unit = unit;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }
}
