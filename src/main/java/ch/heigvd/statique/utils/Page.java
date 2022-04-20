package ch.heigvd.statique.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import ch.heigvd.statique.convertors.HtmlConvertor;

public class Page {
  private Path fromPath;
  private Path toPath;
  private Config pageConf;

  /**
   * Page constructor.
   *
   * @param fromPath The path to the markdown file.
   * @param toPath   The path to the HTML file.
   */
  public Page(Path fromPath, Path toPath) {
    this.fromPath = fromPath;
    this.toPath = toPath;
  }

  /**
   * Render the markdown to HTML and save it to the destination.
   */
  public void render(Config config) throws IOException {
    String fileContent = readFile();
    String[] yamlMd = separateYamlMd(fileContent);
    // TODO parse yaml into pageConf
    String html = convertMd(yamlMd[1]);
    writeFile(html);
  }

  /**
   * Read the markdown file.
   *
   * @return The file content.
   */
  private String readFile() throws IOException {
    return FileUtils.readFileToString(fromPath.toFile(), StandardCharsets.UTF_8);
  }

  /**
   * Write the HTML to the destination.
   *
   * @param content The HTML to write.
   */
  private void writeFile(String content) throws IOException {
    FileUtils.writeStringToFile(toPath.toFile(), content, StandardCharsets.UTF_8);
  }

  /**
   * Separate the YAML and the markdown.
   *
   * @param fileContent The file content.
   * @return An array of 2 elements, the first one is the YAML, the second one
   *         is the markdown.
   */
  private String[] separateYamlMd(String fileContent) {
    return new String[] {"", fileContent};
  }

  /**
   * Convert the markdown to HTML and apply the template.
   *
   * @param md The markdown to convert.
   * @return The HTML.
   */
  private String convertMd(String md) {
    // TODO render with template
    return HtmlConvertor.fromMarkdown(md);
  }
}
