package ch.heigvd.statique.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import ch.heigvd.statique.utils.Utils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "init", description = "Initialize a static site directory")
public class Init implements Callable<Integer> {

    private static final String DEFAULT_DIR_NAME = "default/";

    private static final LinkedList<String> DEFAULT_FILE_LIST = new LinkedList<String>() {
        {
            add("index.md");
            add("config.yaml");
            add("template/layout.hbs");
            add("template/menu.hbs");
        }
    };

    @Parameters(paramLabel = "SITE", description = "The site to build")
    public Path site;

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
