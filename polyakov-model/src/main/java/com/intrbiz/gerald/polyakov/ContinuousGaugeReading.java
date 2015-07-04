package com.intrbiz.gerald.polyakov;

public interface ContinuousGaugeReading<T> extends GaugeReading<T>
{
    T getWarning();
    
    void setWarning(T value);
    
    T getCritical();
    
    void setCritical(T value);
    
    T getMin();
    
    void setMin(T value);
    
    T getMax();
    
    void setMax(T value);
}
