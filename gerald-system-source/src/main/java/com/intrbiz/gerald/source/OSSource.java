package com.intrbiz.gerald.source;

import org.hyperic.sigar.OperatingSystem;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class OSSource extends AbstractIntelligenceSource
{
    public OSSource()
    {
        super("os-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(OSSource.class, "os-arch"), this.archGague());
        registry.register(Witchcraft.name(OSSource.class, "os-name"), this.nameGague());
        registry.register(Witchcraft.name(OSSource.class, "os-description"), this.descriptionGague());
        registry.register(Witchcraft.name(OSSource.class, "os-machine"), this.machineGague());
        registry.register(Witchcraft.name(OSSource.class, "os-version"), this.versionGague());
        registry.register(Witchcraft.name(OSSource.class, "os-patchlevel"), this.patchLevelGague());
        registry.register(Witchcraft.name(OSSource.class, "os-vendor"), this.vendorGague());
        registry.register(Witchcraft.name(OSSource.class, "os-vendor-name"), this.vendorNameGague());
        registry.register(Witchcraft.name(OSSource.class, "os-vendor-code-name"), this.vendorCodeNameGague());
        registry.register(Witchcraft.name(OSSource.class, "os-vendor-version"), this.vendorVersionGague());
    }

    protected Gauge<String> archGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getArch();
            }
        };
    }
    
    protected Gauge<String> machineGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getMachine();
            }
        };
    }
    
    protected Gauge<String> nameGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getName();
            }
        };
    }
    
    protected Gauge<String> descriptionGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getDescription();
            }
        };
    }
    
    protected Gauge<String> vendorGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getVendor();
            }
        };
    }
    
    protected Gauge<String> vendorNameGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getVendorName();
            }
        };
    }
    
    protected Gauge<String> vendorCodeNameGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getVendorCodeName();
            }
        };
    }
    
    protected Gauge<String> vendorVersionGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getVendorVersion();
            }
        };
    }
    
    protected Gauge<String> versionGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getVersion();
            }
        };
    }
    
    protected Gauge<String> patchLevelGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String getValue()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getPatchLevel();
            }
        };
    }
}
