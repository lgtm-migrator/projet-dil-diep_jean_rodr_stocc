package ch.heigvd.statique.convertors;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HtmlConvertor {
    /**
     * Read the markdown file and gets the lines
     * @param filepath File path
     * @return Lines for markdown file
     */
    static String readMarkdown(String filepath) throws IOException{
        StringBuilder mdText = new StringBuilder();
        try (BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8))) {
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
     * Creates an HTML file with the given lines
     * @param filepath File path
     * @param filename File name (without extension)
     * @param htmlLines HTML text using '\n' separators
     * @throws IOException
     */
    public void createHtmlFile(String filepath, String filename, String htmlLines)
            throws IOException {

    }
}
