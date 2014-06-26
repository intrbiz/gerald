
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
            .lamplighter("some_key")
            .from(Node.service("SimpleGerald"))
            .period(30, TimeUnit.SECONDS)
            .courierTo("ws://127.0.0.1:8825/websocket")
            .start();
    }
}
