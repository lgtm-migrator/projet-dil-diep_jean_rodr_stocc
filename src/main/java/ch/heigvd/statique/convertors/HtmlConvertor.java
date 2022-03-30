package ch.heigvd.statique.convertors;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;

public class HtmlConvertor {
    /**
     * Read the markdown file and gets the lines
     * @param filepath File path
     * @return Lines for markdown file
     */
    private String readMarkdown(String filepath){
        return null;
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
