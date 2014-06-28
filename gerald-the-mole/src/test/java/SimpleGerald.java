
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intrbiz.gerald.Gerald;

public class SimpleGerald
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.TRACE);
        // setup Gerald to report
        Gerald.theMole().period(10, TimeUnit.SECONDS).start();
    }
}
