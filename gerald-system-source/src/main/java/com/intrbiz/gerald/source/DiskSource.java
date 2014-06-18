package com.intrbiz.gerald.source;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class DiskSource extends AbstractIntelligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public DiskSource()
    {
        super("disk-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        try
        {
            FileSystem[] fileSystems = sigar.getFileSystemList();
            //
            for (FileSystem fs : fileSystems)
            {
                if (2 == fs.getType())
                {
                    final String mnt  = fs.getDirName();
                    final String dev  = fs.getDevName();
                    final String type = fs.getSysTypeName();
                    //
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-device", mnt), new Gauge<String>(){
                        @Override
                        public String getValue()
                        {
                            return dev;
                        }
                    });
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-type", mnt), new Gauge<String>(){
                        @Override
                        public String getValue()
                        {
                            return type;
                        }
                    });
                    // usage
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-size", mnt), this.totalSpaceGague(mnt));
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-available", mnt), this.availableSpaceGague(mnt));
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-used", mnt), this.usedSpaceGague(mnt));
                    registry.register(Witchcraft.scoped(DiskSource.class, "fs-used-percent", mnt), this.usedPercentGague(mnt));
                }
            }
        }
        catch (SigarException e)
        {
        }
    }
    
    protected Gauge<Long> totalSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long getValue()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getTotal() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> availableSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long getValue()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getFree() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> usedSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long getValue()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getUsed() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> usedPercentGague(final String dir)
    {
        return new Gauge<Double>() {
            @Override
            public Double getValue()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getUsePercent();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
