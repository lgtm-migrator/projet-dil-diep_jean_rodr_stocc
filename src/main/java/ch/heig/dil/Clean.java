package ch.heig.dil;

import picocli.CommandLine.Command;
import java.util.concurrent.Callable;

@Command(name = "clean")
public class Clean implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("Command clean");
        return 0;
    }
}
