package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.Server;
import ch.heigvd.statique.utils.Watcher;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;


@Command(name = "serve", description = "Serve a static site.\n Access the server using : " +
        "http://localhost:<PORT>/")
public class Serve implements Callable<Integer> {
    @Parameters(paramLabel = "SITE", description = "The site to serve")
    public Path site;
    @Parameters(paramLabel = "PORT", description = "The server port number")
    public int port;

    @Option(names = { "--watch"}, description = "Keeps building the site when changes occurred")
    private boolean haveWatcher = false;

    @Override
    public Integer call() throws IOException {
        // Constantly builds the site when changes occurred
        if (haveWatcher){
            new Thread(new Watcher(site)).start();
        }

        // Creates server
        Server server = new Server(site.resolve("build"), port);
        server.start();
        return 0;
    }
}
