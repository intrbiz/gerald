package com.intrbiz.gerald.polyakov;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.intrbiz.Util;

/**
 * The identifier of a Gerald instance
 */
public class Node
{
    public static final String SYSTEM_SERVICE = "System";
    
    private final UUID hostId;
    
    private final String hostName;
    
    private final String service;
    
    public Node(UUID hostId, String hostName, String service)
    {
        super();
        this.hostId = hostId;
        this.hostName = hostName;
        this.service = service;
    }

    public UUID getHostId()
    {
        return this.hostId;
    }

    public String getHostName()
    {
        return this.hostName;
    }

    public String getService()
    {
        return this.service;
    }
    
    public String toString()
    {
        return "Node(" + this.hostId + "; " + this.hostName + "; " + this.service + ")";
    }

    public static Node service(String service)
    {
        // get the host name
        String hostName = System.getProperty("gerald.host_name");
        if (Util.isEmpty(hostName))
        {
            try
            {
                hostName = InetAddress.getLocalHost().getHostName();
            }
            catch (UnknownHostException e)
            {
                throw new RuntimeException("Unable to get host name, please set the proprty: gerald.host_name.");
            }
        }
        //
        String hostId = System.getenv("gerald.host_id");
        if (Util.isEmpty(hostId))
        {
            hostId = UUID.randomUUID().toString();
            Logger.getLogger(Node.class).warn("Generating random host UUID, please consider setting the property: gerald.host_id.");
        }
        //
        return new Node(UUID.fromString(hostId), hostName, service);
    }
    
    public static Node service()
    {
        String service = System.getProperty("gerald.service");
        if (Util.isEmpty(service)) throw new RuntimeException("Could not get the servive name, please set the property: gerald.service.");
        return service(SYSTEM_SERVICE);
    }
    
    public static Node systemService()
    {
        return service(SYSTEM_SERVICE);
    }
}
