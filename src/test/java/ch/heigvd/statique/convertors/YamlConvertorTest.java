package ch.heigvd.statique.convertors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class YamlConvertorTest {
    private Path root;
    private final LinkedList<Path> filesPath = new LinkedList<>();
    private final LinkedList<LinkedList<String>> filesText = new LinkedList<>();
    private final LinkedList<Map<String, Object>> filesMap = new LinkedList<>();
    private static final LinkedList<String> defaultText = new LinkedList<>(Arrays.asList(
            "titre: Mon premier article",
            "auteur: Jean François",
            "date: 2021-03-10",
            "chiffre: 25"
    ));
    private static final Map<String, Object> defaultMap = new HashMap<>() {{
        put("titre", "Mon premier article");
        put("auteur", "Jean François");
        put("date", "2021-03-10");
        put("chiffre", 25);
    }};

    /**
     * Writes inside a file
     *
     * @param filePath file path
     * @param lines    text on each lines
     * @throws IOException BufferWritter exception
     */
    private void writeFile(String filePath, LinkedList<String> lines) throws IOException {
    }

    /**
     * Creates files for test
     *
     * @throws IOException File creation exception
     */
    @BeforeAll
    public void createFiles() throws IOException {
    }

    /**
     * Clear test files
     *
     * @throws IOException File delete exception
     */
    @AfterAll
    public void clearFiles() throws IOException {
    }

    /**
     * Test file reading
     *
     * @throws IOException File reading exception
     */
    @Test
    public void readFile() throws IOException {
    }
}