package ch.heigvd.statique.convertors;

import org.yaml.snakeyaml.Yaml;

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
    public static Map<String, Object> read(String filepath) throws IOException {
        HashMap<String, Object> properties;
        try (FileInputStream fis = new FileInputStream(new File(".").getCanonicalPath()
                + File.separator + filepath
        )) {
            Yaml yaml = new Yaml();
            properties = yaml.load(fis);
        }

        return properties;
    }
}
