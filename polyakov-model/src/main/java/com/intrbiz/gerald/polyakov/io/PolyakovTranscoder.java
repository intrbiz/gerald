package com.intrbiz.gerald.polyakov.io;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intrbiz.gerald.polyakov.CounterReading;
import com.intrbiz.gerald.polyakov.HistogramReading;
import com.intrbiz.gerald.polyakov.MeterReading;
import com.intrbiz.gerald.polyakov.Node;
import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.TimerReading;
import com.intrbiz.gerald.polyakov.gauge.BooleanGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.DoubleGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.FloatGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.IntegerGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.LongGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.StringGaugeReading;

public class PolyakovTranscoder
{
    public static final Class<?>[] CLASSES = {
        Node.class,
        Parcel.class,
        CounterReading.class,
        MeterReading.class,
        TimerReading.class,
        HistogramReading.class,
        StringGaugeReading.class,
        LongGaugeReading.class,
        IntegerGaugeReading.class,
        DoubleGaugeReading.class,
        FloatGaugeReading.class,
        BooleanGaugeReading.class  
    };
    
    private final ObjectMapper factory;
    
    public PolyakovTranscoder()
    {
        super();
        this.factory = new ObjectMapper();
        this.factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.factory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.factory.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.factory.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.factory.registerSubtypes(CLASSES);
    }
    
    public String encodeAsString(Object value) throws IOException
    {
        return this.factory.writeValueAsString(value);
    }
    
    public byte[] encodeAsBytes(Object value) throws IOException
    {
        return this.factory.writeValueAsBytes(value);
    }
    
    public <T> T decodeFromString(String json, Class<T> type) throws IOException
    {
        return this.factory.readValue(json, type);
    }
    
    public <T> T decodeFromBytes(byte[] json, Class<T> type) throws IOException
    {
        return this.factory.readValue(json, type);
    }
}
