package com.totalchange.discodj;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public final class DiscoDjConfiguration {
    private static final Path HOME = Paths.get(System.getProperty("user.home"));
    private static final DiscoDjConfiguration INSTANCE = new DiscoDjConfiguration();

    private final String searchIndex;
    private final String mediaSource;
    
    private DiscoDjConfiguration() {
        final Optional<Map<String, Object>> config = loadConfig();

        this.searchIndex = grabValue(config, "searchIndex", HOME.resolve("./discodj/search-index").toString());
        this.mediaSource = grabValue(config, "mediaSource", HOME.resolve("./Music").toString());
    }
    
    public String getSearchIndex() {
        return searchIndex;
    }
    
    public String getMediaSource() {
        return mediaSource;
    }

    public static DiscoDjConfiguration getInstance() {
        return INSTANCE;
    }

    private Optional<Map<String, Object>> loadConfig() {
        final Path configFile = HOME.resolve("./.discodj/discodj.yaml");
        System.out.println(configFile.toAbsolutePath());
        if (Files.exists(configFile)) {
            return Optional.of(loadConfig(configFile));
        } else {
            return Optional.empty();
        }
    }

    private Map<String, Object> loadConfig(final Path configFile) {
        final Yaml yaml = new Yaml();
        try (final InputStream in = new FileInputStream(configFile.toFile())) {
            return yaml.load(in);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String grabValue(final Optional<Map<String, Object>> config, final String key,
                             final String defaultValue) {
        if (config.isPresent()) {
            final String fromConfig = (String) config.get().get(key);
            if (fromConfig == null) {
                return defaultValue;
            } else {
                return fromConfig;
            }
        } else {
            return defaultValue;
        }
    }
}
