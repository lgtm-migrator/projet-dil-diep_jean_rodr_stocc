package ch.heig.dil;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "starter", version = "starter 1.0",
         subcommands = {
             New.class,
             Serve.class,
             Build.class,
             Clean.class
         })
public class Starter implements Callable<Integer>{
    public static void main(String... args) {
        int exitCode = new CommandLine(new Starter()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Main");
        return 0;
    }
}
