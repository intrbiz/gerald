package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class HostNameSource extends AbstractIntelligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public HostNameSource()
    {
        super("hostname-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(HostNameSource.class, "hostname"), this.hostNameGague());
    }
    
    protected Gauge<String> hostNameGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    return sigar.getFQDN();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
