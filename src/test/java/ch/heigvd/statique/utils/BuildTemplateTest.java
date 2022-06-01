package ch.heigvd.statique.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import ch.heigvd.statique.convertors.Builder;
import org.junit.jupiter.api.*;

public class BuildTemplateTest {
  private static final LinkedList<Path> filesPath = new LinkedList<>();
  private static final LinkedList<String> filesContent = new LinkedList<>();
  private Path testFolder;


  private static final String indexHTML = "<html lang=\"en\">\n" +
          "<head>\n" +
          "    <meta charset=\"utf-8\">\n" +
          "    <title>Mon site internet | Home page</title>\n" +
          "</head>\n" +
          "<body>\n" +
          "    <ul>\n" +
          "    <!-- Paths only work on http server -->\n" +
          "    <li><a href=\"/index.html\">home</a></li>\n" +
          "    <li><a href=\"/pages/page-1.html\">page 1</a></li>\n" +
          "    <li><a href=\"/pages/page-2.html\">page 2</a></li>\n" +
          "</ul>\n" +
          "\n" +
          "    <h1>Mon site internet</h1>\n" +
          "<h2>Titre 2</h2>\n" +
          "<p>poum</p>\n" +
          "\n" +
          "</body>\n" +
          "</html>";

  private static final String page1 = "<html lang=\"en\">\n" +
          "<head>\n" +
          "    <meta charset=\"utf-8\">\n" +
          "    <title>Mon site internet | Page 1</title>\n" +
          "</head>\n" +
          "<body>\n" +
          "    <ul>\n" +
          "    <!-- Paths only work on http server -->\n" +
          "    <li><a href=\"/index.html\">home</a></li>\n" +
          "    <li><a href=\"/pages/page-1.html\">page 1</a></li>\n" +
          "    <li><a href=\"/pages/page-2.html\">page 2</a></li>\n" +
          "</ul>\n" +
          "\n" +
          "    <h1>Titre 1</h1>\n" +
          "<h2>Sous-titre</h2>\n" +
          "<p>Paragraphe et tout.</p>\n" +
          "<p><img src=\"./image.png\" alt=\"doja\" /></p>\n" +
          "\n" +
          "</body>\n" +
          "</html>";

  private static final String page2 = "<html lang=\"en\">\n" +
          "<head>\n" +
          "    <meta charset=\"utf-8\">\n" +
          "    <title>Mon site internet | Page 2</title>\n" +
          "</head>\n" +
          "<body>\n" +
          "    <ul>\n" +
          "    <!-- Paths only work on http server -->\n" +
          "    <li><a href=\"/index.html\">home</a></li>\n" +
          "    <li><a href=\"/pages/page-1.html\">page 1</a></li>\n" +
          "    <li><a href=\"/pages/page-2.html\">page 2</a></li>\n" +
          "</ul>\n" +
          "\n" +
          "    <h1>Titre 1</h1>\n" +
          "<h2>Titre 2</h2>\n" +
          "<p>C'est le paragraphe</p>\n" +
          "<p><img src=\"./doja2.jpeg\" alt=\"doja\" /></p>\n" +
          "\n" +
          "</body>\n" +
          "</html>";

  /**
   * Writes inside a file
   *
   * @param filePath file path
   * @param text     file text (using \n separators)
   * @throws IOException BufferWritter exception
   */
  private static void writeFile(String filePath, String text) throws IOException {
    try (BufferedWriter out = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
      out.write(text);
    }
  }

  /**
   * Creates files for test
   *
   * @throws IOException File creation exception
   */
  @BeforeEach
  public void CreateTestFiles() throws IOException {
    Path p = Path.of("site");
    Builder builder = new Builder(p, p.resolve("build"));
    builder.build();

    testFolder = Files.createTempDirectory("build_test");

    filesPath.add(Files.createFile(testFolder.resolve("index.html")));
    filesPath.add(Files.createFile(testFolder.resolve("page-1.html")));
    filesPath.add(Files.createFile(testFolder.resolve("page-2.html")));
    filesContent.add(indexHTML);
    filesContent.add(page1);
    filesContent.add(page2);

    for (int i = 0; i < 3; i++) {
      writeFile(String.valueOf(filesPath.get(i)), filesContent.get(i));
    }
  }

  /**
   * Erase test files and test folder
   *
   */
  @AfterEach
  public void EraseTestFiles() throws IOException {
    for (Path path : filesPath) {
      Files.deleteIfExists(path);
    }

    Files.deleteIfExists(testFolder);
  }

  /**
   * Test the creation of HTML file from template.
   *
   * @throws IOException File read/write exception
   */
  @Test
  void testCreateHTMLFromTemplate() throws IOException {
    for (int i = 0; i < 3; i++) {
      assertEquals(Files.readString(filesPath.get(i), StandardCharsets.UTF_8), filesContent.get(i));
    }
  }
}
