package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class InterfaceSource extends AbstractIntelligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public InterfaceSource()
    {
        super("interface-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        try
        {
            for (String iface : sigar.getNetInterfaceList())
            {
                registry.register(Witchcraft.name(InterfaceSource.class, "hardware-address", iface), this.hwAddressGague(iface));
                registry.register(Witchcraft.name(InterfaceSource.class, "address", iface), this.addressGague(iface));
                registry.register(Witchcraft.name(InterfaceSource.class, "netmask", iface), this.netmaskGague(iface));
                registry.register(Witchcraft.name(InterfaceSource.class, "type", iface), this.typeGague(iface));
                //
                registry.register(Witchcraft.name(InterfaceSource.class, "tx-bytes", iface), this.txBytesGague(iface));
                registry.register(Witchcraft.name(InterfaceSource.class, "rx-bytes", iface), this.rxBytesGague(iface));
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
            public String getValue()
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
            public String getValue()
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
            public String getValue()
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
            public String getValue()
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
            public Long getValue()
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
            public Long getValue()
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
