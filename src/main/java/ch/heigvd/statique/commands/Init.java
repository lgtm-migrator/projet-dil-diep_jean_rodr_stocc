package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.Utils;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/** This command initializes a static site directory. */
@Command(name = "init", description = "Initialize a static site directory")
public class Init implements Callable<Integer> {

    /** The default directory name */
    private static final String DEFAULT_DIR_NAME = "default/";

    /** The default list of file */
    private static final LinkedList<String> DEFAULT_FILE_LIST =
            new LinkedList<String>() {
                {
                    add("index.md");
                    add("config.yaml");
                    add("template/layout.hbs");
                    add("template/menu.hbs");
                }
            };

    /** The site to build */
    @Parameters(paramLabel = "SITE", description = "The site to build")
    public Path site;

    /** Initializes the static site directory */
    @Override
    public Integer call() throws URISyntaxException, IOException {
        // Verify that destination is a directory.
        boolean fileExist = Files.exists(site);
        if (fileExist && !Files.isDirectory(site)) {
            System.err.println("Destination exists and is not a folder.");
            return -1;
        }

        // Create the directory if it does not exist.
        if (!fileExist) {
            Files.createDirectories(site);
        }

        // Copy the default site structure.
        for (String fileName : DEFAULT_FILE_LIST) {
            Path file = site.resolve(fileName);

            // If the file already exists, skip it.
            if (Files.exists(file)) {
                System.out.println("File " + fileName + " already exists.");
                continue;
            }

            Utils.copyFileFromResources(DEFAULT_DIR_NAME + fileName, file);
        }

        return 0;
    }
}
