package ch.heigvd.statique.utils;

import ch.heigvd.statique.convertors.HtmlConvertor;
import ch.heigvd.statique.convertors.YamlConvertor;

import com.github.jknack.handlebars.Template;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/** This class reads markdown file and generates html pages from it. */
public class Page {
    /** The path to the markdown file */
    private final Path fromPath;
    /** The path to the HTML file */
    private final Path toPath;
    /** The page configuration */
    private Config pageConf;
    /** The template */
    private final Template template;

    /**
     * Page constructor.
     *
     * @param fromPath The path to the markdown file.
     * @param toPath The path to the HTML file.
     * @param temp The template used by the page
     */
    public Page(Path fromPath, Path toPath, Template temp) {
        this.fromPath = fromPath;
        this.toPath = toPath;
        this.template = temp;
    }

    /**
     * Render the markdown to HTML and save it to the destination.
     *
     * @param config The site configuration
     */
    public void render(Config config) throws IOException {
        if (config == null) {
            throw new RuntimeException("Config should not be null");
        }

        String fileContent = readFile();
        String[] yamlMd = separateYamlMd(fileContent);
        String html;

        pageConf = new Config("config", config);

        // Markdown file may not have a yaml part (if it's a new one)
        if (yamlMd.length == 0) {
            html = "";
        } else if (yamlMd.length == 1) {
            html = convertMd(yamlMd[0]);
        } else {
            // Merge site configuration with page configuration
            pageConf.put("page", YamlConvertor.fromString(yamlMd[0]));
            html = convertMd(yamlMd[1]);
        }

        String result = buildFromTemplate(html);
        writeFile(result);
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
     * Separate the YAML and the markdown. Warning : The markdown needs to contain a yaml bloc code
     *
     * @param fileContent The file content.
     * @return An array of 2 elements, the first one is the YAML, the second one is the markdown.
     */
    private String[] separateYamlMd(String fileContent) {
        return fileContent.split("\r?\n ?--- ?\r?\n", 2);
    }

    /**
     * Convert the markdown to HTML and apply the template.
     *
     * @param md The markdown to convert.
     * @return The HTML.
     */
    private String convertMd(String md) throws IOException {
        String html = HtmlConvertor.fromMarkdown(md);
        return HtmlConvertor.renderHtml(html, pageConf);
    }

    private String buildFromTemplate(String html) throws IOException {
        pageConf.put("content", html);
        return template.apply(pageConf.toRender());
    }
}
