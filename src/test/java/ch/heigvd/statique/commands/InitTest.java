package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class InitTest {
    Path root, notADir, isAFile;

    /**
     * Set up destination dir
     *
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        root = Files.createTempDirectory("statique_");
        isAFile = Files.createTempFile("statique_file_", ".test");
        notADir = root.resolve("not_a_dir");
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(root.toFile());
        Files.deleteIfExists(isAFile);
    }

    @Test
    void inDirFilesCreated() {
        new CommandLine(new Init()).execute(root.toString());

        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        assertTrue(Files.isRegularFile(index));
        assertTrue(Files.isRegularFile(config));
    }

    @Test
    void filesContainsSomething() throws FileNotFoundException, IOException {
        new CommandLine(new Init()).execute(root.toString());

        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        assertNotEquals(index.toFile().length(), 0);
        assertNotEquals(config.toFile().length(), 0);
    }

    @Test
    void inNotADirFilesCreated() {
        new CommandLine(new Init()).execute(notADir.toString());

        Path index = notADir.resolve("index.md");
        Path config = notADir.resolve("config.yaml");

        assertTrue(Files.isDirectory(notADir));
        assertTrue(Files.isRegularFile(index));
        assertTrue(Files.isRegularFile(config));
    }

    @Test
    void inAFile() throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));
            new CommandLine(new Init()).execute(isAFile.toString());

            assertTrue(output.toString().contains("Destination exists and is not a folder."));
        }
    }

    @Test
    void fileIndexDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        Files.createFile(index);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File index.md already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            assertEquals(index.toFile().length(), 0);
            assertNotEquals(config.toFile().length(), 0);
        }
    }

    @Test
    void fileConfigDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        Files.createFile(config);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File config.yaml already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            assertNotEquals(index.toFile().length(), 0);
            assertEquals(config.toFile().length(), 0);
        }
    }

    @Test
    void fileIndexAndConfigDoesExist() throws Exception {
        Path index = root.resolve("index.md");
        Path config = root.resolve("config.yaml");

        Files.createFile(index);
        Files.createFile(config);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Init()).execute(root.toString());

            assertTrue(output.toString().contains("File index.md already exists."));
            assertTrue(output.toString().contains("File config.yaml already exists."));

            assertTrue(Files.exists(index));
            assertTrue(Files.exists(config));

            assertEquals(index.toFile().length(), 0);
            assertEquals(config.toFile().length(), 0);
        }
    }
}
