package ch.heigvd.statique.convertors;

import org.yaml.snakeyaml.Yaml;
import ch.heigvd.statique.utils.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class YamlConvertor {

    /**
     * Reads a YAML file to a Config object
     *
     * @param filepath File path
     * @return Config object
     * @throws IOException Couldn't load properties
     */
    public static Config fromFile(File filepath) throws IOException {
        HashMap<String, Object> properties;
        try (FileInputStream fis = new FileInputStream(filepath)) {
            Yaml yaml = new Yaml();
            properties = yaml.load(fis);
        }

        return new Config(properties);
    }

    /**
     * Convert a YAML string to a Config object.
     *
     * @param yaml YAML string
     * @return Config object
     */
    public static Config fromString(String yaml) {
        Yaml yamlParser = new Yaml();
        HashMap<String, Object> properties = yamlParser.load(yaml);

        return new Config(properties);
    }
}
