
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intrbiz.gerald.Gerald;
import com.intrbiz.gerald.polyakov.PolyakovKey;
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
            /*.source(new JVMSource())*/
            .source(new CPUSource())
            .source(new MemorySource())
            .source(new HostNameSource())
            .source(new UptimeSource())
            .source(new OSSource())
            .source(new DiskSource())
            .source(new NetworkSource())
            .source(new InterfaceSource())
            .json()
            .http()
            .from(UUID.randomUUID(), "test", "system-gerald")
            .key(new PolyakovKey("SHA512:XosfmtImdhE6YxgwPntlYxVUomK4xK5rOBO4tq43n70c3HqGJLGkEUmCXrC8VuSZTFtakFwMNq1Q4hMu9tWUuaYT9urv2CPzxaOYDqQ5n1T5hzHBM7wmXz7YtyOoxo7poXMDztfXba2NcdkjYZICgVnafwZSTVQpuh0dUCafSTtNOKC1857MGTuyAE66zQlPFLpkoxP0VJ6ZWA77q7cxB7FAcvztykGiAlemGswokuP6hIokTVCpMzryqKjvHDCuqAih9Yit++nUKbpVAGbqiGCdZ1daTfWHo1mGIf5sB/squWvOf9/tO5ROOUua8o2hKapnL7UdW6iWLl08J8OEGe/M19VdBQkRcxSWfLBZfWiSdJvGT6B4cbdiCn3qGTA/WCImSKpYiDyPAx90pWbFsVMHjgIwLPPgoFRNVzPkEZYDlk45xcHwMQ7n1E6BnVYkMPUOXaaebZCGg9jrjLqfIjAgVbIbPDkM5xrls7TMju/GVh1V2HsYShf/rLf0Y2uPohOPG9ZQ8R4jyKff8RpolvLagyJeXTBZVbrsyja8t7/fbS1t0XTtcfmuLw1qtacfm5uDDeKeJKuPPr7muCzjFL+Xdla4zIgzFaORqFUJPOmdo6+2nqB/MbOupe8BwPBEMhMQK0cpK6DA71XMkj1gXoU7eTkLrFJHfmNybRYZnSATK3EBvwXT7BJJ1slD2CS1UAb8wFPe7yizzSCnBGrNp9D98/r1dy0nll0sZpsuIPPMzR+HNtV0UQcz1Pi497J4qDGSody6lGNugi2U/57JhrWTGR4rmRkMkaCgzA6m0R2nhYWlqGk74Cl8JQ9HBHdFz3PNtkSdfV5sT7FvT+4rMxrj2FEMyRyCXSU7ZcLh8UlT0CS9nnf+BaTsBdASL2z9oVNF5tO2E4PP3zT/nDB8QmZLNwvXCGVtUc01ZZYab4AGqG44e5w33yfc4jg7qvTraUkgNaZHaNm/+uKlrAwC0VFNop4fUIEiYxNLKyGlvveWyTLaC4FVCJ8RPTEPNtUl7kE6hb2t5qdoyOmlChtYBwcwN6/D8R4K2AHYZT5jn+DfX3ZQU7J2gQYmX6n+3XV4MNrfgHc7BqDmGw7/ocROY9eypnjUDNIUpRRTaWRJL2kqn8C67WM+xbMFJ0OYAMi9px9zrxWx8zaGmQItCFDvnGwMY8lcFM8h7MQyp7Fb/PwnkhiPpPAAATcXHbD27lDbFnxPUPAu5vheN3w1LMmPoP2CivgKzRCeAQUq5x+pjqzJlU+DiuPEhNPKurkHd1ccUCpFVcx6owDEmaUEcSfk4uUkJxMAx1tgGjuTlaX5EMUnRzr3rBwj3iBwZdaxX2fdRNoQnPAqnOeazY/P1ZRNfQ=="))
            .period(5, TimeUnit.SECONDS)
            .courierTo("http://localhost/lamplighter/receive")
            .start();
    }
}
