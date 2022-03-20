package ch.heigvd.statique.commands;

import java.io.IOException;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {

    @Override
    public Integer call() throws IOException {
        System.out.println("Command build");
        return 0;
    }
}