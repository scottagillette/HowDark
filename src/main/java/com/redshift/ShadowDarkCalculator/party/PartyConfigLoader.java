package com.redshift.ShadowDarkCalculator.party;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class PartyConfigLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private PartyConfigLoader() {}

    public static PartyConfig load(Path path) {
        try {
            final PartyConfig config = MAPPER.readValue(path.toFile(), PartyConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read party config: " + path, e);
        }
    }

    public static PartyConfig load(InputStream inputStream) {
        try {
            final PartyConfig config = MAPPER.readValue(inputStream, PartyConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read party config from stream", e);
        }
    }

    public static PartyConfig parse(String yaml) {
        try {
            final PartyConfig config = MAPPER.readValue(yaml, PartyConfig.class);
            config.validate();
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse party config", e);
        }
    }
}
