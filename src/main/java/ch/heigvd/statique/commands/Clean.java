package ch.heigvd.statique.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import ch.heigvd.statique.utils.Utils;

@Command(name = "clean", description = "Clean a statique site")
public class Clean implements Callable<Integer> {

    @Parameters(paramLabel = "SITE", description = "The site to clean")
    public Path site;

    @Override
    public Integer call() throws IOException {
        // Check that the site folder exists.
        if (!Files.exists(site)) {
            System.err.println("Destination does not exists");
            return -1;
        }

        // Verify that destination is a folder.
        if (!Files.isDirectory(site)) {
            System.err.println("Destination is not a folder");
            return -1;
        }

        // Get build folder.
        Path buildDir = site.resolve("build");

        // Delete the folder if it exists.
        if (Files.exists(buildDir)) {
            Utils.deleteRecursive(buildDir);
            System.out.println("Build folder cleaned");
        } else {
            System.out.println("Nothing to clean");
        }

        return 0;
    }
}
