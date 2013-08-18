package com.intrbiz.gerald.polyakov;

import com.intrbiz.util.Optioned;

/**
 * How to transport the packaged metrics to their destination
 */
public interface Transport extends Optioned
{
    void to(String url);
    
    void courier(Parcel parcel);
}
