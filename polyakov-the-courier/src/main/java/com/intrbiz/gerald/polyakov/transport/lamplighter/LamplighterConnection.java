package com.intrbiz.gerald.polyakov.transport.lamplighter;

import java.io.IOException;

public interface LamplighterConnection
{
    boolean isConnected();
    
    void send(Object message) throws IOException;
    
    void close();
}
