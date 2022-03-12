package ch.heig.dil;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "new")
public class New implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("Command new");
        return 0;
    }


}
