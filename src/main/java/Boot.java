import com.google.gson.Gson;
import com.google.inject.Guice;
import com.typesafe.config.Config;
import com.typesafe.scalalogging.LazyLogging;
import com.typesafe.scalalogging.Logger;
import scutum.core.contracts.endpointprotection.IDataScanner;

// todo fix logger - should not be infrastructure ?
// todo check @inject annotation
// todo add dependency to scutum endpoint protection
// todo check performance to new java api
// todo fix code clean
// todo push git
public class Boot implements LazyLogging {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Boot.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Gson serializer = new Gson();
        logger.info("starting application");

        com.google.inject.Injector injector = Guice.createInjector(new Injector());
        logger.info("di created");

        Config config = injector.getInstance(Config.class);
        logger.info("config loaded");

        IDataScanner scanner = injector.getInstance(IDataScanner.class);

        while (true) {
            Thread.sleep(config.getLong("conf.interval"));
            String data = serializer.toJson(scanner.scan());
            logger.info("something happened " + data);
        }
    }

    @Override
    public Logger logger() {
        return null;
    }
}

