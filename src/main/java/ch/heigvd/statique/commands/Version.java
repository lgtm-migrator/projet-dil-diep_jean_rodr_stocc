package ch.heigvd.statique.commands;

import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "--version", description = "Show the generator's version in terminal")
public class Version implements Callable<Integer> {
    @Override
    public Integer call() throws IOException {
        try {
            String v = Files.readString(Path.of("src/about/version.txt"));
            System.out.println("Statique Version " + v);
        } catch (Exception e){
            System.err.println("Error, generator version is not provided.");
            return -1;
        }

        return 0;
    }
}
