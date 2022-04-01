package ch.heigvd.statique.convertors;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlConvertor {
    /**
     * Read a file and gets the lines
     * @param filepath File path
     * @return Lines from file
     */
    public static String readFile(String filepath) throws IOException{
        StringBuilder mdText = new StringBuilder();
        try (BufferedReader fis = new BufferedReader(new InputStreamReader(
                new FileInputStream(filepath), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = fis.readLine()) != null){
                mdText.append(line).append('\n');
            }
        }
        return mdText.toString();
    }

    /**
     * Converts markdown to HTML
     * from : https://simplesolution.dev/java-parse-markdown-to-html-using-commonmark/
     * @param markdown Markdown file lines with '\n' separators
     * @return HTML lines
     */
    public static String convertMarkdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        return htmlRenderer.render(document);
    }

    /**
     * Creates an HTML file using a markdown file
     * @param markdownPath Markdown file path (file should use '\n' separator)
     * @param htmlPath Markdown file path
     * @param htmlFileName HTML file name (with extension)
     * @throws IOException Couldn't read or create file
     */
    public static void createHtmlFileFromMarkdown(String markdownPath, String htmlPath, String htmlFileName)
            throws IOException {
        Files.createFile(Path.of(htmlPath + htmlFileName));
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(htmlPath + htmlFileName), StandardCharsets.UTF_8)))
        {
            out.write(convertMarkdownToHTML(readFile(markdownPath)));
        }
    }
}
