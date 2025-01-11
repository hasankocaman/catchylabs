package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Logger logger = LogManager.getLogger(Configuration.class);
    private static Configuration instance;
    private Properties properties;
    private static final String CONFIG_FILE = "config.properties";
    private static final String ENV_CONFIG_FILE = "config/%s.config.properties";

    private Configuration() {
        loadProperties();
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        String environment = System.getProperty("env", "test");

        try {
            // Environment specific config'i yükle
            String envConfigFile = String.format(ENV_CONFIG_FILE, environment);
            loadFromClasspath(envConfigFile);

            // Genel config'i yükle
            loadFromClasspath(CONFIG_FILE);

            // System property'lerini ekle
            properties.putAll(System.getProperties());

            logger.info("Configuration loaded successfully for environment: {}", environment);
        } catch (IOException e) {
            logger.error("Error loading configuration files", e);
            throw new RuntimeException("Could not load configuration files", e);
        }
    }

    private void loadFromClasspath(String resourceName) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.debug("Loaded properties file from classpath: {}", resourceName);
            } else {
                logger.warn("Could not find properties file in classpath: {}", resourceName);
            }
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: {}", key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Integer tipinde property değerini döndürür, bulunamazsa varsayılan değeri döndürür
     * @param key property anahtarı
     * @param defaultValue varsayılan değer
     * @return integer değer veya varsayılan değer
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Could not parse integer property: {}, using default value: {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Integer tipinde property değerini döndürür
     * @param key property anahtarı
     * @return integer değer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            if (value == null) {
                throw new RuntimeException("Property not found: " + key);
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Could not parse integer property: {}", key, e);
            throw new RuntimeException("Invalid integer property: " + key, e);
        }
    }


    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    public void reload() {
        loadProperties();
        logger.info("Configuration reloaded");
    }
}