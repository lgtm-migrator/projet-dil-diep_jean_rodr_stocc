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

import ch.heigvd.statique.convertors.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import ch.heigvd.statique.utils.Utils;
import picocli.CommandLine;

public class BuildTest {
  Path build, dirNotExist;

  /**
   * Make the build
   *
   * @throws IOException File creation exception
   */
  @BeforeEach
  void makeBuild() throws IOException {
    Path p = Path.of("site");
    Builder builder = new Builder(p, p.resolve("build"));
    builder.build();

    build = p;

    // Directory path that does not exist
    dirNotExist = build.resolve("not_a_folder");
  }

  /**
   * Clean up the build folder made only to test if builder's working
   */
  @AfterEach
  void EraseFolder() throws IOException {
    Utils.deleteRecursive(Path.of("site/build"));
  }

  /**
   * Test to build with the build dir
   */
  @Test
  void buildWithBuild() throws Exception {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      System.setOut(new PrintStream(output));

      int exitCode = new CommandLine(new Build()).execute(build.toString());
      assertEquals(0, exitCode);
      assertTrue(output.toString().contains("Build done"));
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

      int exitCode = new CommandLine(new Build()).execute("site/build/index.html");
      assertEquals(-1, exitCode);
      assertTrue(output.toString().contains("Destination is not a folder"));
    }
  }
}
