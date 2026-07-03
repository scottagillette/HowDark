package com.redshift.ShadowDarkCalculator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Loads a FightConfig from YAML. Unknown fields fail fast so typos are caught early.
 */

public final class FightConfigLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private FightConfigLoader() {
    }

    public static FightConfig load(Path path) {
        try {
            final FightConfig config = MAPPER.readValue(path.toFile(), FightConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read fight config: " + path, e);
        }
    }

    public static FightConfig load(InputStream inputStream) {
        try {
            final FightConfig config = MAPPER.readValue(inputStream, FightConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read fight config from stream", e);
        }
    }

    public static FightConfig parse(String yaml) {
        try {
            final FightConfig config = MAPPER.readValue(yaml, FightConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse fight config", e);
        }
    }

}
