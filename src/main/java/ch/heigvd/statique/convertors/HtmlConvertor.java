package ch.heigvd.statique.convertors;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class HtmlConvertor {

    /**
     * Converts markdown to HTML from :
     * https://simplesolution.dev/java-parse-markdown-to-html-using-commonmark/
     *
     * @param markdown Markdown file lines with '\n' separators
     * @return HTML lines
     */
    public static String fromMarkdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        return htmlRenderer.render(document);
    }
}
