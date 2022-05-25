package ch.heigvd.statique.commands;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "publish", description = "Publish the site")
public class Publish implements Callable<Integer> {
  @Parameters(paramLabel = "SITE", description = "The site to build")
  public Path site;

  @Parameters(paramLabel = "DESTINATION", description = "The destination folder")
  public String uri;

  public Integer call() {
    throw new UnsupportedOperationException("Not implemented yet.");
  }
}
