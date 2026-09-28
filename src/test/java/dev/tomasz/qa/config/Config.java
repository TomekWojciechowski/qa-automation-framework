package dev.tomasz.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration. Resolution order: system property, environment variable, config.properties.
 * Example: mvn test -Dbrowser=firefox -Dheadless=false
 */
public final class Config {

    private static final Properties FILE = load();

    private Config() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config.properties", e);
        }
        return props;
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(key.toUpperCase().replace('.', '_'));
        if (env != null && !env.isBlank()) {
            return env;
        }
        return FILE.getProperty(key);
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String remoteUrl() {
        String url = get("remote.url");
        return url == null || url.isBlank() ? null : url;
    }

    public static String uiBaseUrl() {
        return get("ui.base.url");
    }

    public static String apiBaseUrl() {
        return get("api.base.url");
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeout.seconds"));
    }
}
