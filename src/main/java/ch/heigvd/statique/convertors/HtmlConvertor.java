package ch.heigvd.statique.convertors;

import ch.heigvd.statique.utils.Config;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;

/** This class converts Markdown syntax to html syntax. */
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

    /**
     * Applies the a template in a html file using the template engine Handlebars
     * @param html the content of the html file
     * @param pageConf the config containing the variables called in the template
     * @return the html content after applying the template
     * @throws IOException Couldn't find the pageConf file
     */
    public static String renderHtml(String html, Config pageConf) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(html);
        return template.apply(pageConf.toRender());
    }
}
