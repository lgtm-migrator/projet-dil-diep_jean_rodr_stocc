package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CleanTest {
    Path root, dir, file1, file2;
    Path buildRoot, buildPagesDir, buildIndex, buildPage;
    Path dirNotExist;

    /** Set up the directory for each test */
    @BeforeEach
    void setUp() throws IOException {
        root = Files.createTempDirectory("statique_");
        dir = Files.createDirectories(root.resolve("dir"));
        file1 = Files.createFile(dir.resolve("file1"));
        file2 = Files.createFile(root.resolve("file2"));

        // Do not create build dir by default.
        buildRoot = root.resolve("build");
        buildPagesDir = buildRoot.resolve("pages");
        buildIndex = buildRoot.resolve("index.html");
        buildPage = buildPagesDir.resolve("page1.html");

        // Directory path that does not exist
        dirNotExist = root.resolve("not_a_folder");
    }

    /** Delete all temporary files. */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(buildPage);
        Files.deleteIfExists(buildIndex);
        Files.deleteIfExists(buildPagesDir);
        Files.deleteIfExists(buildRoot);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file1);
        Files.deleteIfExists(dir);
        Files.deleteIfExists(root);
    }

    /** Create the build directory with the needed files. */
    void createBuild() throws IOException {
        Files.createDirectories(buildRoot);
        Files.createDirectories(buildPagesDir);
        Files.createFile(buildIndex);
        Files.createFile(buildPage);
    }

    /** Verify that the directory is correctly cleaned without touching other files. */
    void assertCleaned() {
        assertTrue(Files.exists(root));
        assertTrue(Files.exists(dir));
        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));

        // Build dir must not exist.
        assertFalse(Files.exists(buildRoot));
        assertFalse(Files.exists(buildPagesDir));
        assertFalse(Files.exists(buildIndex));
        assertFalse(Files.exists(buildPage));
    }

    /** Test to clean without the build dir. */
    @Test
    void cleanNoBuild() throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));

            int exitCode = new CommandLine(new Clean()).execute(root.toString());
            assertEquals(0, exitCode);
            assertTrue(output.toString().contains("Nothing to clean"));
            assertCleaned();
        }
    }

    /** Test to clean with the build dir */
    @Test
    void cleanWithBuild() throws Exception {
        createBuild();
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));

            int exitCode = new CommandLine(new Clean()).execute(root.toString());
            assertEquals(0, exitCode);
            assertTrue(output.toString().contains("Build folder cleaned"));
            assertCleaned();
        }
    }

    /** Test to clean in a folder that does not exist. */
    @Test
    void cleanFolderNotExist() throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));

            int exitCode = new CommandLine(new Clean()).execute(dirNotExist.toString());
            assertEquals(-1, exitCode);
            assertTrue(output.toString().contains("Destination does not exists"));
        }
    }

    /** Test to clean on a file. */
    @Test
    void cleanOnAFile() throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));

            int exitCode = new CommandLine(new Clean()).execute(file1.toString());
            assertEquals(-1, exitCode);
            assertTrue(output.toString().contains("Destination is not a folder"));
        }
    }
}
