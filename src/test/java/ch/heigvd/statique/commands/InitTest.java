package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import picocli.CommandLine;

public class InitTest {
    Path root, notADir, isAFile;

    /**
     * Set up destination dir
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        root = Files.createTempDirectory("statique_");
        isAFile = Files.createTempFile("statique_file_", "test");
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
    void filesContainsSomething() {
        new CommandLine(new Init()).execute(root.toString());
        throw new UnsupportedOperationException();
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

    void inAFiles() throws Exception{
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));
            new CommandLine(new Init()).execute(isAFile.toString());

            assertTrue(output.toString().contains("Destination exists and is not a folder."));
        }
    }
}
