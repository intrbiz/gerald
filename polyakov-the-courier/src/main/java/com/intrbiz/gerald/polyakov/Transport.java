package com.intrbiz.gerald.polyakov;

import java.util.UUID;

import com.intrbiz.util.Optioned;

/**
 * How to transport the packaged metrics to their destination
 */
public interface Transport extends Optioned
{
    void to(String url);
    
    void node(UUID id, String name, String service);
    
    void key(PolyakovKey key);
    
    void courier(Parcel parcel);
}
