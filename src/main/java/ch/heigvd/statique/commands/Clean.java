package ch.heigvd.statique.commands;

import picocli.CommandLine.Command;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "clean", description = "Clean a statique site")
public class Clean implements Callable<Integer> {

    @Override
    public Integer call() throws IOException {
        System.out.println("Command clean");
        return 0;
    }
}
