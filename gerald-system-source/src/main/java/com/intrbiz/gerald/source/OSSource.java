package com.intrbiz.gerald.source;

import org.hyperic.sigar.OperatingSystem;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class OSSource extends InteligenceSource
{
    public OSSource()
    {
        super("os-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        registry.newGauge(OSSource.class, "os-arch", this.archGague());
        registry.newGauge(OSSource.class, "os-name", this.nameGague());
        registry.newGauge(OSSource.class, "os-description", this.descriptionGague());
        registry.newGauge(OSSource.class, "os-machine", this.machineGague());
        registry.newGauge(OSSource.class, "os-version", this.versionGague());
        registry.newGauge(OSSource.class, "os-patchlevel", this.patchLevelGague());
        registry.newGauge(OSSource.class, "os-vendor", this.vendorGague());
        registry.newGauge(OSSource.class, "os-vendor-name", this.vendorNameGague());
        registry.newGauge(OSSource.class, "os-vendor-code-name", this.vendorCodeNameGague());
        registry.newGauge(OSSource.class, "os-vendor-version", this.vendorVersionGague());
    }

    protected Gauge<String> archGague()
    {
        return new Gauge<String>()
        {
            @Override
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
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
            public String value()
            {
                OperatingSystem sys = OperatingSystem.getInstance();
                return sys.getPatchLevel();
            }
        };
    }
}
