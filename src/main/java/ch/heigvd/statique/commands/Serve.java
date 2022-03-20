package ch.heigvd.statique.commands;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Command serve");
        return 0;
    }
}
