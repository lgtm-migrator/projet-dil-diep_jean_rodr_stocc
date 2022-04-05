package ch.heigvd.statique;

import ch.heigvd.statique.commands.*;

import java.io.InputStream;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "statique",
    description = "A brand new static site generator.",
    subcommands = {
      Init.class,
      Clean.class,
      Build.class,
      Serve.class,
    })
public class Statique implements Callable<Integer> {
  @CommandLine.Option(names = "--version", description = "Show the generator's version in terminal")
  boolean version;

  public static void main(String... args) {
    int exitCode = new CommandLine(new Statique()).execute(args);
    if (exitCode != 0) {
      System.exit(exitCode);
    }
  }

  @Override
  public Integer call() throws Exception {
    if(version) {
      try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("about/version.txt")) {
        String v = new String(inputStream.readAllBytes());
        System.out.println("Statique Version " + v);
      } catch (Exception e){
        System.err.println("Error, generator version is not provided.");
        return -1;
      }
    } else {
      CommandLine.usage(this, System.out);
    }
    return 0;
  }
}
