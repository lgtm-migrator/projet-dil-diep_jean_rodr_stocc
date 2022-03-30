package ch.heigvd.statique.convertors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class YamlConvertorTest {
    private static Path root;
    private static final LinkedList<Path> filesPath = new LinkedList<>();
    private static final LinkedList<LinkedList<String>> filesText = new LinkedList<>();
    private static final LinkedList<Map<String, Object>> filesMap = new LinkedList<>();
    private static final LinkedList<String> defaultText = new LinkedList<>(Arrays.asList(
            "titre: Mon premier article",
            "auteur: Jean François",
            "date: 2021-03-10",
            "chiffre: 25"
    ));
    private static final Map<String, Object> defaultMap = new HashMap<>() {{
        put("titre", "Mon premier article");
        put("auteur", "Jean François");
        put("date", new Yaml().loadAs("2021-03-10", Date.class));
        put("chiffre", 25);
    }};

    /**
     * Writes inside a file
     *
     * @param filePath file path
     * @param lines    text on each lines
     * @throws IOException BufferWritter exception
     */
    private static void writeFile(String filePath, LinkedList<String> lines) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8
        ))) {
            for (String line : lines) {
                out.write(line);
                out.newLine();
            }
        }
    }

    /**
     * Creates files for test
     *
     * @throws IOException File creation exception
     */
    @BeforeAll
    static void createFiles() throws IOException {
        root = Files.createTempDirectory("yamlConvertor_");

        filesPath.add(Files.createFile(root.resolve("config1.yml")));
        filesText.add(new LinkedList<>(defaultText));
        filesMap.add(defaultMap);
        writeFile(filesPath.getLast().toString(), filesText.getLast());

        filesPath.add(Files.createFile(root.resolve("config1.yaml")));
        filesText.add(new LinkedList<>(defaultText));
        filesMap.add(defaultMap);
        writeFile(filesPath.getLast().toString(), filesText.getLast());
    }

    /**
     * Clear test files
     *
     * @throws IOException File delete exception
     */
    @AfterAll
    static void clearFiles() throws IOException {
        for (Path path : filesPath) {
            Files.deleteIfExists(path);
        }
    
        Files.deleteIfExists(root);
    }

    /**
     * Test file reading
     *
     * @throws IOException File reading exception
     */
    @Test
    void readFile() throws IOException {
        for (int i = 0; i < filesPath.size(); ++i) {
            assertEquals(
                    filesMap.get(i),
                    YamlConvertor.read(filesPath.get(i).toString())
            );
        }
    }
}