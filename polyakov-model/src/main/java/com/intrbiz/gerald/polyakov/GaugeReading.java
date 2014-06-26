package com.intrbiz.gerald.polyakov;

public interface GaugeReading<T>
{
    T getValue();
    
    void setValue(T value);
}
