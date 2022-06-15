package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitTest {
    Path root, notADir, isAFile;

    /**
     * Set up destination dir and file
     *
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        root = Files.createTempDirectory("statique_");
        isAFile = Files.createTempFile("statique_file_", ".test");
        notADir = root.resolve("not_a_dir");
    }

    /** Clean up temporary dir and file */
    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(root.toFile());
        Files.deleteIfExists(isAFile);
    }

    /** Test that the command create the needed files. */
    @Test
    void inDirFilesCreated() {
        new CommandLine(new Init()).execute(root.toString());

        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        assertTrue(Files.isRegularFile(index));
        assertTrue(Files.isRegularFile(config));
    }

    /** Test that the command create not empty files. */
    @Test
    void filesContainsSomething() throws FileNotFoundException, IOException {
        new CommandLine(new Init()).execute(root.toString());

        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        assertNotEquals(index.toFile().length(), 0);
        assertNotEquals(config.toFile().length(), 0);
    }

    /**
     * Test that the command create the needed directory and files if the directory does not yet
     * exist.
     */
    @Test
    void inNotADirFilesCreated() {
        new CommandLine(new Init()).execute(notADir.toString());

        Path index = notADir.resolve("index.md");
        Path config = notADir.resolve("config.yaml");

        assertTrue(Files.isDirectory(notADir));
        assertTrue(Files.isRegularFile(index));
        assertTrue(Files.isRegularFile(config));
    }

    /** Test that the command fail if the destination is a file. */
    @Test
    void inAFile() throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));
            int codeError = new CommandLine(new Init()).execute(isAFile.toString());

            assertEquals(codeError, -1);
            assertTrue(output.toString().contains("Destination exists and is not a folder."));
        }
    }

    /** Test that the command does not create the config file if it already exists. */
    @Test
    void fileIndexDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        // Create the empty file
        Files.createFile(index);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File index.md already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            // Index must be empty but config must not.
            assertEquals(index.toFile().length(), 0);
            assertNotEquals(config.toFile().length(), 0);
        }
    }

    /** Test that the command does not create the config file if it already exists. */
    @Test
    void fileConfigDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        // Create the empty file
        Files.createFile(config);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File config.yaml already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            // Config must be empty but index must not.
            assertNotEquals(index.toFile().length(), 0);
            assertEquals(config.toFile().length(), 0);
        }
    }

    /** Test that the command does not create the index and config file if they already exist. */
    @Test
    void fileIndexAndConfigDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        // Create the empty file
        Files.createFile(index);
        Files.createFile(config);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File index.md already exists."));
            assertTrue(output.toString().contains("File config.yaml already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            // Index and config must be empty.
            assertEquals(index.toFile().length(), 0);
            assertEquals(config.toFile().length(), 0);
        }
    }
}
