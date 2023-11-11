package ch.hslu.appe.micro;

import ch.hslu.appe.bus.RabbitMqConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger LOG = LogManager.getLogger(ApplicationConfig.class);
    private static final String DB_URL = "db.url";
    private static final String DB_PORT = "db.port";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String CONFIG_FILE_NAME = "application.properties";

    private final Properties properties = new Properties();

    /**
     * Reads the configuration from the default file application.properties.
     */
    public ApplicationConfig() {
        this(CONFIG_FILE_NAME);
    }

    /**
     * Constructor
     *
     * @param fileName      name of the config file
     */
    ApplicationConfig(final String fileName){
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            LOG.error("Error while reading from file {}", CONFIG_FILE_NAME);
        }
    }

    public String getDbUrl() {
        return this.properties.getProperty(DB_URL);
    }

    public int getDbPort() {
        return Integer.parseInt(this.properties.getProperty(DB_PORT));
    }

    public String getDbUser() {
        return this.properties.getProperty(DB_USER);
    }

    public String getDbPassword() {
        return this.properties.getProperty(DB_PASSWORD);
    }
}
