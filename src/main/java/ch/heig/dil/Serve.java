package ch.heig.dil;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "serve")
public class Serve implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("Command serve");
        return 0;
    }
}
