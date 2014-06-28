
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intrbiz.gerald.Gerald;
import com.intrbiz.gerald.polyakov.Node;

public class SimpleGerald
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.TRACE);
        // setup Gerald to report
        Gerald.theMole()
            .lamplighter()
            .from(Node.service("SimpleGerald"))
            .start();
    }
}
