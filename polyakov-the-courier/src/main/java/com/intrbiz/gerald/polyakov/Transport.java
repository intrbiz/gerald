package com.intrbiz.gerald.polyakov;

/**
 * How to transport the packaged metrics to their destination
 */
public interface Transport
{
    void to(String url);
    
    void courier(Parcel parcel);
    
    void start();
    
    //
    
    void option(Option<String> option, String value);

    void option(Option<Integer> option, int value);

    void option(Option<Long> option, long value);

    void option(Option<Float> option, float value);

    void option(Option<Double> option, double value);

    void option(Option<Boolean> option, boolean value);
}
