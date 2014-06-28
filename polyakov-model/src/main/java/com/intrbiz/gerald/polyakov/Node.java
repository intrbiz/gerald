package com.intrbiz.gerald.polyakov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
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

    /**
     * Construct a node identifier with the given service name and 
     * auto-detecting the host name and id.
     * 
     * The host name is determined by:
     *   1. The system property "gerald.host.name"
     *   2. The local host name
     *   
     * The host UUID is determined by:
     *   1. The system property "gerals.host.id"
     *   2. The file /etc/gerald.host.id
     *   3. Randomly generated
     * 
     * 
     * @param service the service name
     */
    public static Node service(String service)
    {
        // get the host name
        String hostName = System.getProperty("gerald.host.name");
        if (hostName == null || hostName.length() <= 0)
        {
            try
            {
                hostName = InetAddress.getLocalHost().getHostName();
            }
            catch (UnknownHostException e)
            {
                throw new RuntimeException("Unable to get host name, please set the proprty: gerald.host.name.");
            }
        }
        //
        String hostId = System.getenv("gerald.host.id");
        if (hostId == null || hostId.length() <= 0)
        {
            hostId = readSystemHostId();
        }
        if (hostId == null || hostId.length() <= 0)
        {
            hostId = UUID.randomUUID().toString();
            Logger.getLogger(Node.class).warn("Generating random host UUID, please consider setting the property: gerald.host.id or creating the file /etc/gerald.host.id");
        }
        //
        return new Node(UUID.fromString(hostId), hostName, service);
    }

    /**
     * Construct a node identifier by auto-detecting the service name,
     * the host name and host id.
     * 
     * The service name is determined by:
     *   1. The "gerald.service" system property
     *   2. The PID (on Linux)
     *   3. The JMX Runtime name as taken from RuntimeMXBean
     * 
     * @return
     */
    public static Node service()
    {
        // figure out our sevice name
        String serviceName = System.getProperty("gerald.service");
        // try to get the PID (Linux only)
        if (serviceName == null || serviceName.length() <= 0)
        {
            try
            {
                serviceName = "PID::" + new File("/proc/self").getCanonicalFile().getName();
            }
            catch (IOException e)
            {
            }
        }
        // failback to JMX
        if (serviceName == null || serviceName.length() <= 0)
        {
            serviceName = ManagementFactory.getRuntimeMXBean().getName();
        }
        return service(serviceName);
    }

    /**
     * Construct a node identifier using the system service name.
     * Note: You shouldn't use this in your application.
     */
    public static Node systemService()
    {
        return service(SYSTEM_SERVICE);
    }
    
    /**
     * Read the value of /etc/gerald.host.id
     * @return null if the file does not exist of is invalid
     */
    public static String readSystemHostId()
    {
        try
        {
            File file = new File("/etc/gerald.host.id");
            if (file.exists())
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try 
                {
                    String id = reader.readLine();
                    if (id != null) return UUID.fromString(id.trim()).toString();
                }
                finally
                {
                    reader.close();
                }
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }
}
