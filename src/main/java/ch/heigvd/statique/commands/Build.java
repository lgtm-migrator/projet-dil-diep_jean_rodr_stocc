package ch.heigvd.statique.commands;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.nio.file.Files;
import java.nio.file.Path;
import ch.heigvd.statique.convertors.Builder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {
    @Parameters(paramLabel = "SITE", description = "The site to build")
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

        Builder builder = new Builder(site, site.resolve("build"));
        builder.build();
        System.out.println("Build done");

        return 0;
    }
}