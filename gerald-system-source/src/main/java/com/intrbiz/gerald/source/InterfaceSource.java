package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class InterfaceSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public InterfaceSource()
    {
        super("interface-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        try
        {
            for (String iface : sigar.getNetInterfaceList())
            {
                registry.newGauge(InterfaceSource.class, "hardware-address", iface, this.hwAddressGague(iface));
                registry.newGauge(InterfaceSource.class, "address", iface, this.addressGague(iface));
                registry.newGauge(InterfaceSource.class, "netmask", iface, this.netmaskGague(iface));
                registry.newGauge(InterfaceSource.class, "type", iface, this.typeGague(iface));
                //
                registry.newGauge(InterfaceSource.class, "tx-bytes", iface, this.txBytesGague(iface));
                registry.newGauge(InterfaceSource.class, "rx-bytes", iface, this.rxBytesGague(iface));
            }
        }
        catch (SigarException e)
        {
        }
    }
    
    protected Gauge<String> addressGague(final String iface)
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    NetInterfaceConfig nic = sigar.getNetInterfaceConfig(iface);
                    return nic.getAddress();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> netmaskGague(final String iface)
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    NetInterfaceConfig nic = sigar.getNetInterfaceConfig(iface);
                    return nic.getNetmask();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> hwAddressGague(final String iface)
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    NetInterfaceConfig nic = sigar.getNetInterfaceConfig(iface);
                    return nic.getHwaddr();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> typeGague(final String iface)
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    NetInterfaceConfig nic = sigar.getNetInterfaceConfig(iface);
                    return nic.getType();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> txBytesGague(final String iface)
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    NetInterfaceStat nis = sigar.getNetInterfaceStat(iface);
                    return nis.getTxBytes();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> rxBytesGague(final String iface)
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    NetInterfaceStat nis = sigar.getNetInterfaceStat(iface);
                    return nis.getRxBytes();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
