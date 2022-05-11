package ch.heigvd.statique.commands;

import ch.heigvd.statique.Statique;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionTest {

    @Test
    void show() throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(output));
            new CommandLine(new Statique()).execute("--version");
            assertTrue((output.toString().contains("Statique Version")));
        }
    }

}
