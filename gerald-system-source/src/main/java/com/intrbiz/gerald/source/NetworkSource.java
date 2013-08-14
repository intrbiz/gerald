package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class NetworkSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public NetworkSource()
    {
        super("network-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        registry.newGauge(NetworkSource.class, "default-gateway", this.defaultGatewayGague());
        registry.newGauge(NetworkSource.class, "domain-name", this.domainNameGague());
        registry.newGauge(NetworkSource.class, "host-name", this.hostNameGague());
        registry.newGauge(NetworkSource.class, "primary-dns", this.primaryDNSGague());
        registry.newGauge(NetworkSource.class, "secondary-dns", this.secondaryGatewayGague());
        //
        registry.newGauge(NetworkSource.class, "all-inbound-traffic", this.allInboundTotalGague());
        registry.newGauge(NetworkSource.class, "all-outbound-traffic", this.allOutboundTotalGague());
    }
    
    protected Gauge<String> defaultGatewayGague()
    {
        return new Gauge<String>() {
            @Override
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public Integer value()
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
            public Integer value()
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
