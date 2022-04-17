package ch.heigvd.statique.commands;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.nio.file.Path;
import ch.heigvd.statique.convertors.Builder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {
    @Parameters(paramLabel = "SITE", description = "The site to build")
    public Path site;

    @Override
    public Integer call() throws IOException {
        Builder builder = new Builder(site, site.resolve("build"));
        builder.build();

        return 0;
    }
}