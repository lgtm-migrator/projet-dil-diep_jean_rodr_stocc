package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.Utils;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/** This command deletes a static site's folder. */
@Command(name = "clean", description = "Clean a static site")
public class Clean implements Callable<Integer> {

    /** The site to clean */
    @Parameters(paramLabel = "SITE", description = "The site to clean")
    public Path site;

    /** Delete the given folder by calling deleteRecursive from utils. */
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
