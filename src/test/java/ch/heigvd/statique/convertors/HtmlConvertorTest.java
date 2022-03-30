package ch.heigvd.statique.convertors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlConvertorTest {
    private static Path root;
    private static final LinkedList<Path> filesPath = new LinkedList<>();
    private static final LinkedList<String> filesMDText = new LinkedList<>();
    private static final LinkedList<String> filesHtmlText = new LinkedList<>();
    // Test from https://simplesolution.dev/java-parse-markdown-to-html-using-commonmark/
    private static final String defaultMD =
            "# heading h1\n" +
            "## heading h2\n" +
            "### heading h3\n" +
            "#### heading h4\n";
    private static final String defaultHTML =
            "<h1>heading h1</h1>\n" +
            "<h2>heading h2</h2>\n" +
            "<h3>heading h3</h3>\n" +
            "<h4>heading h4</h4>\n" +
            "<hr />\n";

    /**
     * Writes inside a file
     *
     * @param filePath file path
     * @param text    file text (using \n separators)
     * @throws IOException BufferWritter exception
     */
    private static void writeFile(String filePath, String text) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8
        ))) {
            out.write(text);
        }
    }

    /**
     * Creates files for test
     *
     * @throws IOException File creation exception
     */
    @BeforeAll
    static void createFiles() throws IOException {
        root = Files.createTempDirectory("htmlConvertor_");

        filesPath.add(Files.createFile(root.resolve("test.md")));
        filesMDText.add(defaultMD);
        filesHtmlText.add(defaultHTML);
        writeFile(filesPath.getLast().toString(), filesMDText.getLast());
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
    void readMarkdown() throws IOException {
        for (int i = 0; i < filesPath.size(); ++i) {
            assertEquals(
                    filesMDText.get(i),
                    HtmlConvertor.readMarkdown(filesPath.get(i).toString())
            );
        }
    }
}
