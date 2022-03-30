package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilsTest {

  Path root, dir1, dir2, dir3, file1, file2, file3;

  /**
   * Set up the directories to test the deletion.
   */
  @BeforeEach
  void setUp() throws IOException {
    root = Files.createTempDirectory("statique_");
    dir1 = Files.createDirectories(root.resolve("dir1"));
    dir2 = Files.createDirectories(root.resolve("dir2"));
    dir3 = Files.createDirectories(dir2);
    file1 = Files.createFile(dir1.resolve("file1"));
    file2 = Files.createFile(dir2.resolve("file2"));
    file3 = Files.createFile(dir3.resolve("file3"));
  }

  /**
   * Clean up the directories if needed.
   */
  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(file3);
    Files.deleteIfExists(file2);
    Files.deleteIfExists(file1);
    Files.deleteIfExists(dir3);
    Files.deleteIfExists(dir2);
    Files.deleteIfExists(dir1);
    Files.deleteIfExists(root);
  }

  /**
   * Test to clean the directory recursively.
   */
  @Test
  public void deleteRecursive() throws IOException {
    Utils.deleteRecursive(root);
    assertFalse(Files.exists(root));
    assertFalse(Files.exists(dir1));
    assertFalse(Files.exists(dir2));
    assertFalse(Files.exists(dir3));
    assertFalse(Files.exists(file1));
    assertFalse(Files.exists(file2));
    assertFalse(Files.exists(file3));
  }

}
