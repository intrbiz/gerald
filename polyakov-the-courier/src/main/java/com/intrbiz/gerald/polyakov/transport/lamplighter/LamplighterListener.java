package com.intrbiz.gerald.polyakov.transport.lamplighter;

public abstract class LamplighterListener
{
    public void onConnect(LamplighterConnection connection) {}
    
    public void onDisconnect(LamplighterConnection connection) {}
    
    public void onError(LamplighterConnection connection, Throwable error) {}
    
    public abstract void onMessage(LamplighterConnection connection, Object message);
}
