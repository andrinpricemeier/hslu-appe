package ch.hslu.appe.micro;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ApplicationConfig {    
    private static final Logger LOG = LogManager.getLogger(ApplicationConfig.class);
    private static final String DB_URL = "db.url";
    private static final String DB_PORT = "db.port";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String CONFIG_FILE_NAME = "application.properties";

    private final Properties properties = new Properties();

    /**
     * Liest die Konfiguration vom Default-File ein.
     */
    public ApplicationConfig() {
        this(CONFIG_FILE_NAME);
    }

    /**
     * Liest die Konfiguration ein.
     * @param fileName Dateiname.
     */
    ApplicationConfig(final String fileName) {
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
