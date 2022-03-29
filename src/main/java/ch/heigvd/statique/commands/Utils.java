package ch.heigvd.statique.commands;

import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.nio.file.Path;

public class Utils {

  public static void deleteRecursive(Path directory) throws IOException {
    FileUtils.deleteDirectory(directory.toFile());
  }

}