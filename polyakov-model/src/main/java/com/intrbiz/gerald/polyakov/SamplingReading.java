package com.intrbiz.gerald.polyakov;

public interface SamplingReading
{
    ReadingSnapshot getSnapshot();
    
    void setSnapshot(ReadingSnapshot snapshot);
}
