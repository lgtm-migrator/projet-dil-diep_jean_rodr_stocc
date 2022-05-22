package ch.heigvd.statique.convertors;

import ch.heigvd.statique.utils.Config;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;

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

    public static String renderHtml(String html, Config pageConf) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(html);
        return template.apply(pageConf.toRender());
    }
}
