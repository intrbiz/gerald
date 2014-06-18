
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intrbiz.gerald.Gerald;
import com.intrbiz.gerald.polyakov.Node;
import com.intrbiz.gerald.sources.JVMSource;

public class SimpleGerald
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.TRACE);
        // setup Gerald to report
        Gerald.theMole()
            .source(new JVMSource())
            .lamplighter()
            .from(Node.service("SimpleGerald"))
            .period(5, TimeUnit.MINUTES)
            .courierTo("http://hub.lamplighter/receive")
            .start();
    }
}
