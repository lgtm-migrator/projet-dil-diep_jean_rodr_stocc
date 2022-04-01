package ch.heigvd.statique.convertors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.HTML;
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
    private static final LinkedList<Path> filesMDPath = new LinkedList<>();
    private static final LinkedList<Path> filesHtmlPath = new LinkedList<>();
    private static final LinkedList<String> filesMDText = new LinkedList<>();
    private static final LinkedList<String> filesHtmlText = new LinkedList<>();
    // Test from https://simplesolution.dev/java-parse-markdown-to-html-using-commonmark/
    private static final String defaultMD =
            "# heading h1\n" +
            "## heading h2\n" +
            "### heading h2\n" +
            "#### heading h4\n" +
            "---\n" +
            "- 1st  list\n" +
            "    - second list\n" +
            "        - third list\n" +
            "- end list \n" +
            "---\n" +
            "[<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">](https://www.mozilla.org/)  \n" +
            "[![Foo](https://mdn.github.io/beginner-html-site/images/firefox-icon.png)](https://www.mozilla.org/)  \n" +
            "[Link to mozilla](https://www.mozilla.org/)\n" +
            "I just love **bold text**.  \n" +
            "I just love __bold text__.  \n" +
            "Love**is**bold  \n" +
            "A*cat*meow  \n" +
            "1. First item\n" +
            "3. Second item\n" +
            "8. Third item\n" +
            "    1. Indented item\n" +
            "    9. Indented item\n" +
            "10. Fourth item\n";
    private static final String defaultHTML =
            "<h1>heading h1</h1>\n" +
            "<h2>heading h2</h2>\n" +
            "<h3>heading h2</h3>\n" +
            "<h4>heading h4</h4>\n" +
            "<hr />\n" +
            "<ul>\n" +
            "<li>1st  list\n" +
            "<ul>\n" +
            "<li>second list\n" +
            "<ul>\n" +
            "<li>third list</li>\n" +
            "</ul>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</li>\n" +
            "<li>end list</li>\n" +
            "</ul>\n" +
            "<hr />\n" +
            "<p><a href=\"https://www.mozilla.org/\">" +
                "<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">" +
                "</a><br />\n" +
            "<a href=\"https://www.mozilla.org/\">" +
                "<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\" " +
                "alt=\"Foo\" /></a><br />\n" +
            "<a href=\"https://www.mozilla.org/\">Link to mozilla</a><br />\n" +
            "I just love <strong>bold text</strong>.<br />\n" +
            "I just love <strong>bold text</strong>.<br />\n" +
            "Love<strong>is</strong>bold<br />\n" +
            "A<em>cat</em>meow</p>\n" +
            "<ol>\n" +
            "<li>First item</li>\n" +
            "<li>Second item</li>\n" +
            "<li>Third item\n" +
            "<ol>\n" +
            "<li>Indented item</li>\n" +
            "<li>Indented item</li>\n" +
            "</ol>\n" +
            "</li>\n" +
            "<li>Fourth item</li>\n" +
            "</ol>\n";


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
        filesMDPath.add(Files.createFile(root.resolve("test.md")));
        filesMDText.add(defaultMD);
        filesHtmlText.add(defaultHTML);
        writeFile(filesMDPath.getLast().toString(), filesMDText.getLast());
    }

    /**
     * Clear test files
     *
     * @throws IOException File delete exception
     */
    @AfterAll
    static void clearFiles() throws IOException {
        for (Path path : filesMDPath) {
            Files.deleteIfExists(path);
        }

        for (Path path : filesHtmlPath) {
            Files.deleteIfExists(path);
        }

        Files.deleteIfExists(root);
    }

    /**
     * Test markdown reading
     *
     * @throws IOException File reading exception
     */
    @Test
    void readMarkdown() throws IOException {
        for (int i = 0; i < filesMDPath.size(); ++i) {
            assertEquals(
                    filesMDText.get(i),
                    HtmlConvertor.readFile(filesMDPath.get(i).toString())
            );
        }
    }

    /**
     * Test conversion markdown to HTML
     */
    @Test
    void convertMarkdownToHTML() {
        for (int i = 0; i < filesMDPath.size(); ++i) {
            assertEquals(
                    filesHtmlText.get(i),
                    HtmlConvertor.convertMarkdownToHTML(filesMDText.get(i))
            );
        }
    }


    /**
     * Test html file creation
     *
     * @throws IOException File creation exception
     */
    @Test
    void htmlFileCreation() throws IOException {
        for (int i = 0; i < filesMDPath.size(); ++i) {
            String filepath =  root.toString(), filename = "test" + i + ".html";
            HtmlConvertor.createHtmlFileFromMarkdown(filesMDPath.get(i).toString(),
                    filepath, filename);
            filesHtmlPath.add(Path.of(filepath + filename));
            assertEquals(
                    filesHtmlText.get(i),
                    HtmlConvertor.readFile(String.valueOf(filesHtmlPath.get(i)))
            );
        }
    }
}
