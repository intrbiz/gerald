
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intrbiz.gerald.Gerald;
import com.intrbiz.gerald.polyakov.Node;
import com.intrbiz.gerald.source.CPUSource;
import com.intrbiz.gerald.source.DiskSource;
import com.intrbiz.gerald.source.HostNameSource;
import com.intrbiz.gerald.source.InterfaceSource;
import com.intrbiz.gerald.source.MemorySource;
import com.intrbiz.gerald.source.NetworkSource;
import com.intrbiz.gerald.source.OSSource;
import com.intrbiz.gerald.source.UptimeSource;

public class SystemGerald
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.TRACE);
        //
        Gerald.theMole()
            .source(new CPUSource())
            .source(new MemorySource())
            .source(new HostNameSource())
            .source(new UptimeSource())
            .source(new OSSource())
            .source(new DiskSource())
            .source(new NetworkSource())
            .source(new InterfaceSource())
            .lamplighter()
            .from(Node.systemService())
            .period(30, TimeUnit.SECONDS)
            .courierTo("http://hub.lamplighter/receive")
            .start();
    }
}
