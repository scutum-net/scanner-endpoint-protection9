import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.scalalogging.LazyLogging;
import com.typesafe.scalalogging.Logger;
import net.codingwell.scalaguice.ScalaModule;
import scutum.scanner.endpointprotection.contracts.IDataScanner;
import scutum.scanner.endpointprotection.providers.DataScannerWindows;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Injector extends AbstractModule implements ScalaModule,LazyLogging{

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Boot.class.getName());

    @Provides
    @Singleton
    Config getConfig() {
        File logFile = new File("./app.conf");
        logger.info("config loaded: ${logFile.getCanonicalPath} ${logFile.exists}");
        if (logFile.exists()) {
            return ConfigFactory.parseFile(logFile);
        } else {
            return ConfigFactory.load("app.conf");
        }
    }

    @Override
    protected void configure() {
        File logFile = new File("./logback.xml");
        if (logFile.exists()) try {
            System.setProperty("logback.configurationFile", logFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("logback loaded: ${logFile.getCanonicalPath} ${logFile.exists}");
    }

    //@Inject
    @Provides
    @Singleton
    IDataScanner getScanners(Config config) throws Exception {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String osName = System.getProperty("os.name").toLowerCase();
        //if(osName.startsWith("mac os x")){
        //    new DataScannerMac(hostName, config.getString("conf.customerId"), 1, 1)
        //}
        //else
        if(osName.startsWith("windows")){
            return new DataScannerWindows(hostName, config.getString("conf.customerId"), 1, 2);
        }
        else {
            throw new Exception("unknown os $osName");
        }
    }

    @Override
    public Logger logger() {
        return null;
    }

//    override def configure(): Unit = {
//        val logFile = new File("./logback.xml")
//        if (logFile.exists) System.setProperty("logback.configurationFile", logFile.getCanonicalPath)
//        logger.info(s"logback loaded: ${logFile.getCanonicalPath} ${logFile.exists}")
//    }
//
//    @Provides
//    @Singleton def getConfig: Config = {
//        val logFile = new File("./app.conf")
//        logger.info(s"config loaded: ${logFile.getCanonicalPath} ${logFile.exists}")
//        if (logFile.exists) ConfigFactory.parseFile(logFile) else ConfigFactory.load("app.conf")
//    }
//
//    @Provides
//    @Singleton def getScanners(@Inject config: Config): IDataScanner = {
//        val hostName = InetAddress.getLocalHost().getHostName()
//
//        val osName = System.getProperty("os.name").toLowerCase
//        if(osName.startsWith("mac os x")){
//            new DataScannerMac(hostName, config.getString("conf.customerId"), 1, 1)
//        }
//        else if(osName.startsWith("windows")){
//            new DataScannerWindows(hostName, config.getString("conf.customerId"), 1, 2);
//        }
//        else throw new Exception(s"unknown os $osName")
//    }
}
