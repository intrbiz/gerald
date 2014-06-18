package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class NetworkSource extends AbstractIntelligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public NetworkSource()
    {
        super("network-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(NetworkSource.class, "default-gateway"), this.defaultGatewayGague());
        registry.register(Witchcraft.name(NetworkSource.class, "domain-name"), this.domainNameGague());
        registry.register(Witchcraft.name(NetworkSource.class, "host-name"), this.hostNameGague());
        registry.register(Witchcraft.name(NetworkSource.class, "primary-dns"), this.primaryDNSGague());
        registry.register(Witchcraft.name(NetworkSource.class, "secondary-dns"), this.secondaryGatewayGague());
        //
        registry.register(Witchcraft.name(NetworkSource.class, "all-inbound-traffic"), this.allInboundTotalGague());
        registry.register(Witchcraft.name(NetworkSource.class, "all-outbound-traffic"), this.allOutboundTotalGague());
    }
    
    protected Gauge<String> defaultGatewayGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    NetInfo ni = sigar.getNetInfo();
                    return ni.getDefaultGateway();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> domainNameGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    NetInfo ni = sigar.getNetInfo();
                    return ni.getDomainName();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> hostNameGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    NetInfo ni = sigar.getNetInfo();
                    return ni.getHostName();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> primaryDNSGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    NetInfo ni = sigar.getNetInfo();
                    return ni.getPrimaryDns();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> secondaryGatewayGague()
    {
        return new Gauge<String>() {
            @Override
            public String getValue()
            {
                try
                {
                    NetInfo ni = sigar.getNetInfo();
                    return ni.getSecondaryDns();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }

    protected Gauge<Integer> allInboundTotalGague()
    {
        return new Gauge<Integer>() {
            @Override
            public Integer getValue()
            {
                try
                {
                    NetStat ns = sigar.getNetStat();
                    return ns.getAllInboundTotal();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Integer> allOutboundTotalGague()
    {
        return new Gauge<Integer>() {
            @Override
            public Integer getValue()
            {
                try
                {
                    NetStat ns = sigar.getNetStat();
                    return ns.getAllOutboundTotal();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
}
