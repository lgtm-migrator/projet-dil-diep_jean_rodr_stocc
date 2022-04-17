package ch.heigvd.statique.convertors;

import org.yaml.snakeyaml.Yaml;
import ch.heigvd.statique.utils.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlConvertor {
    /**
     * Reads a YAML file and collects properties.
     *
     * @param filepath File path
     * @return Properties
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
}
