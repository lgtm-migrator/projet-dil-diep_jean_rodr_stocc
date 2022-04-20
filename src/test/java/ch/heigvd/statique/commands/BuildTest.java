package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import ch.heigvd.statique.utils.Utils;
import picocli.CommandLine;

public class BuildTest {
  Path root, build, dirNotExist;
  private final LinkedList<Path> filesMDPath = new LinkedList<>();
  private final LinkedList<Path> filesHtmlPath = new LinkedList<>();
  private final LinkedList<Path> filesYamlPath = new LinkedList<>();
  private final LinkedList<String> filesMDText = new LinkedList<>();
  private final LinkedList<String> filesYamlText = new LinkedList<>();
  private final LinkedList<Map<String, Object>> filesYamlMap = new LinkedList<>();

  /**
   * Writes inside a file
   *
   * @param filePath file path
   * @param text     file text (using \n separators)
   * @throws IOException BufferWriter exception
   */
  private void writeFile(String filePath, String text) throws IOException {
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
  void createFiles() throws IOException {
    root = Files.createTempDirectory("site");
    build = root.resolve("build");

    filesMDPath.add(Files.createFile(root.resolve("index.md")));
    filesHtmlPath.add(build.resolve("index.html"));
    filesMDText.add("# Mon premier article\n" + "## Mon sous-titre\n" + "Le contenu de mon article.\n"
        + "![Une image](./dossier/image.png)\n");

    writeFile(filesMDPath.getLast().toString(), filesMDText.getLast());

    filesYamlPath.add(Files.createFile(root.resolve("config.yaml")));
    filesYamlText
        .add("titre: Mon premier article\n" + "auteur: Jean François\n" + "date: 2021-03-10\n" + "chiffre: 25\n");
    filesYamlMap.addLast(new HashMap<>() {
      {
        put("titre", "Mon premier article");
        put("auteur", "Jean François");
        put("date", new Yaml().loadAs("2021-03-10", Date.class));
        put("chiffre", 25);
      }
    });
    writeFile(filesYamlPath.getLast().toString(), filesYamlText.getLast());
    filesYamlPath.set(filesYamlMap.size() - 1, build.resolve("config.yaml"));

    Files.createDirectories(root.resolve("dossier"));
    filesMDPath.add(Files.createFile(root.resolve("dossier/page.md")));
    filesHtmlPath.add(build.resolve("dossier/page.html"));
    filesMDText.add("# Première page\n");
    writeFile(filesMDPath.getLast().toString(), filesMDText.getLast());

    // Directory path that does not exist
    dirNotExist = root.resolve("not_a_folder");
  }

  /**
   * Clean up temporary directory
   */
  @AfterEach
  void tearDown() throws IOException {
    Utils.deleteRecursive(root);
  }

  /**
   * Test to build with the build dir
   */
  @Test
  void buildWithBuild() throws Exception {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      System.setOut(new PrintStream(output));

      int exitCode = new CommandLine(new Build()).execute(root.toString());
      assertEquals(0, exitCode);
      assertTrue(output.toString().contains("Build done"));

      for (Path path : filesHtmlPath) {
        assertTrue(Files.exists(path));
      }
    }
  }

  /**
   * Test to build in a folder that does not exist.
   */
  @Test
  void buildFolderNotExist() throws Exception {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      System.setErr(new PrintStream(output));

      int exitCode = new CommandLine(new Build()).execute(dirNotExist.toString());
      assertEquals(-1, exitCode);
      assertTrue(output.toString().contains("Destination does not exists"));

    }
  }

  /**
   * Test to build on a file.
   */
  @Test
  void buildOnAFile() throws Exception {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      System.setErr(new PrintStream(output));

      int exitCode = new CommandLine(new Build()).execute(filesMDPath.get(0).toString());
      assertEquals(-1, exitCode);
      assertTrue(output.toString().contains("Destination is not a folder"));
    }
  }
}
