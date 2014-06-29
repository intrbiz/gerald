package com.intrbiz.gerald.polyakov.transport.lamplighter;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.intrbiz.gerald.polyakov.Option;
import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.Transport;

public class LamplighterTransport implements Transport
{
    public static final Option<String> LAMPLIGHTER_KEY = new Option<String>("lamplighter.key");

    private String url;
    
    private String key = null;
    
    private LamplighterConnection connection;
    
    private final LamplighterListener listener;
    
    private final Timer reconnectTimer;
    
    private final Runnable connector;
    
    private Logger logger = Logger.getLogger(LamplighterTransport.class);
    
    private long reconnectDelay = 5000L;

    public LamplighterTransport()
    {
        super();
        //
        this.reconnectTimer = new Timer();
        //
        this.listener = new LamplighterListener()
        {
            @Override
            public void onMessage(LamplighterConnection connection, Object message)
            {
                logger.debug("Got message from Lamplighter: " + message);
            }
            
            @Override
            public void onConnect(LamplighterConnection connection)
            {
                if (logger.isDebugEnabled()) logger.debug("Sucessfully connected to Lamplighter");
                reconnectDelay = 5000L;
            }

            @Override
            public void onDisconnect(LamplighterConnection connection)
            {
                // schedule a reconnect
                connection = null;
                if (logger.isDebugEnabled()) logger.debug("Lamplighter disconnected, scheduling reconnect");
                reconnectDelay = reconnectDelay < 300000L ? reconnectDelay * 2L : reconnectDelay;
                if (logger.isDebugEnabled()) logger.debug("Scheduling reconnect in: " + reconnectDelay + "ms");
                reconnectTimer.schedule(new TimerTask() { public void run() { connector.run(); } }, reconnectDelay);
            }
        };
        //
        this.connector = new Runnable()
        {
            public void run()
            {
                try
                {
                    if (logger.isDebugEnabled()) logger.debug("Connecting to Lamplighter");
                    connection = LamplighterClient.getDefaultInstance().connect(new URI(url), key, listener);
                }
                catch (Exception e)
                {
                   logger.debug("Failed to connect to Lamplighter", e);
                   // reset state
                   connection = null;
                   // schedule a reconnect
                   reconnectDelay = reconnectDelay < 300000L ? reconnectDelay * 2L : reconnectDelay;
                   if (logger.isDebugEnabled()) logger.debug("Scheduling reconnect in: " + reconnectDelay + "ms");
                   reconnectTimer.schedule(new TimerTask() { public void run() { connector.run(); } }, reconnectDelay);
                }
            }
        };
    }

    @Override
    public void option(Option<Integer> option, int value)
    {
    }

    @Override
    public void option(Option<Long> option, long value)
    {
    }

    @Override
    public void option(Option<Float> option, float value)
    {
    }

    @Override
    public void option(Option<Double> option, double value)
    {
    }

    @Override
    public void option(Option<Boolean> option, boolean value)
    {
    }

    @Override
    public void to(String url)
    {
        this.url = url;
    }

    @Override
    public void courier(Parcel parcel)
    {
        if (this.connection != null && this.connection.isConnected())
        {
            try
            {
                this.connection.send(parcel);
            }
            catch (IOException e)
            {
                logger.warn("Error sending parcel", e);
            }
        }
    }

    @Override
    public void start()
    {
        // start the connection attempt
        this.connector.run();
    }

    @Override
    public void option(Option<String> option, String value)
    {
        // Lamplighter key
        if (LAMPLIGHTER_KEY.equals(option))
        {
            this.key = value;
        }
    }

    public String getLamplighterKey()
    {
        return key;
    }

    public void setLamplighterKey(String lamplighterKey)
    {
        this.key = lamplighterKey;
    }
}
