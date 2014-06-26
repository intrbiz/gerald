package com.intrbiz.gerald.polyakov;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The identifier of a Gerald instance
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonTypeName("node")
public class Node
{
    @JsonProperty("host_id")
    private UUID hostId;

    @JsonProperty("host_name")
    private String hostName;

    @JsonProperty("service")
    private String service;

    public Node()
    {
        super();
    }

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

    public void setHostId(UUID hostId)
    {
        this.hostId = hostId;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String toString()
    {
        return "Node(" + this.hostId + ", " + this.hostName + ", " + this.service + ")";
    }
    
    // factories
    
    public static final String SYSTEM_SERVICE = "System";

    public static Node service(String service)
    {
        // get the host name
        String hostName = System.getProperty("gerald.host_name");
        if (hostName == null || hostName.length() <= 0)
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
        if (hostId == null || hostId.length() <= 0)
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
        if (service == null || service.length() <= 0) throw new RuntimeException("Could not get the service name, please set the property: gerald.service.");
        return service(SYSTEM_SERVICE);
    }

    public static Node systemService()
    {
        return service(SYSTEM_SERVICE);
    }
}
