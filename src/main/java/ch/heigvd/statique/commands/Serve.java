package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.Server;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;


@Command(name = "serve", description = "Serve a static site.\n Access the server using : " +
        "http://localhost:<PORT>/index.html")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "SITE", description = "The site to serve")
    public Path site;
    @CommandLine.Parameters(paramLabel = "PORT", description = "The server port number")
    public int port;

    @Override
    public Integer call() throws IOException {
        // Creates server
        Server server = new Server(site, port);
        server.start();
        return 0;
    }
}
