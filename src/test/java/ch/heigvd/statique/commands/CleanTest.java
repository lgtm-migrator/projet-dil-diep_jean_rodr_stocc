package ch.heigvd.statique.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class CleanTest {
    Path root, dir, file1, file2;
    Path buildRoot, buildDir, buildFile1, buildFile2;

    void setUp(boolean withBuild) throws IOException {
        root = Files.createTempDirectory("statique_");
        dir = Files.createDirectories(root.resolve("dir"));
        file1 = Files.createFile(dir.resolve("file1"));
        file2 = Files.createFile(root.resolve("file2"));

        if (withBuild) {
            buildRoot = Files.createDirectories(root.resolve("build"));
            buildDir = Files.createDirectories(buildRoot.resolve("buildDir"));
            buildFile1 = Files.createFile(buildRoot.resolve("buildFile1"));
            buildFile2 = Files.createFile(buildDir.resolve("buildFile2"));
        } else {
            buildRoot = root.resolve("build");
            buildDir = buildRoot.resolve("buildDir");
            buildFile1 = buildRoot.resolve("buildFile1");
            buildFile2 = buildDir.resolve("buildFile2");
        }
    }

    void tearDown() throws IOException {
        Files.deleteIfExists(buildFile2);
        Files.deleteIfExists(buildFile1);
        Files.deleteIfExists(buildDir);
        Files.deleteIfExists(buildRoot);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file1);
        Files.deleteIfExists(dir);
        Files.deleteIfExists(root);
    }

    void assertCleaned() {
        assertTrue(Files.exists(root));
        assertTrue(Files.exists(dir));
        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));
        assertFalse(Files.exists(buildRoot));
        assertFalse(Files.exists(buildDir));
        assertFalse(Files.exists(buildFile1));
        assertFalse(Files.exists(buildFile2));
    }

    @Test
    void cleanNoBuild() throws Exception {
        setUp(false);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));

            new CommandLine(new Clean()).execute(root.toString());
            assertTrue(output.toString().contains("Nothing to clean"));
            assertCleaned();
        }
        tearDown();
    }

    @Test
    void cleanWithBuild() throws Exception {
        setUp(true);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));

            new CommandLine(new Clean()).execute(root.toString());
            assertTrue(output.toString().contains("Build folder cleaned"));
            assertCleaned();
        }
        tearDown();
    }

    @Test
    void cleanFolderNotExist() throws Exception {
        setUp(true);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setErr(new PrintStream(output));

            new CommandLine(new Clean()).execute(root.resolve("not_a_folder").toString());
            assertTrue(output.toString().contains("Destination does not exists"));
        }
        tearDown();
    }
}
