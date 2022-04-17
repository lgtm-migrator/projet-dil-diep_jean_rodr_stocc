package ch.heigvd.statique.convertors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlConvertorTest {
    private static final String defaultMdN = "# heading h1\n" + "## heading h2\n" + "### heading h2\n"
            + "#### heading h4\n" + "---\n" + "- 1st  list\n" + "    - second list\n" + "        - third list\n"
            + "- end list \n" + "---\n"
            + "[<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">](https://www.mozilla.org/)  \n"
            + "[![Foo](https://mdn.github.io/beginner-html-site/images/firefox-icon.png)](https://www.mozilla.org/)  \n"
            + "[Link to mozilla](https://www.mozilla.org/)\n" + "I just love **bold text**.  \n"
            + "I just love __bold text__.  \n" + "Love**is**bold  \n" + "A*cat*meow  \n" + "1. First item\n"
            + "3. Second item\n" + "8. Third item\n" + "    1. Indented item\n" + "    9. Indented item\n"
            + "10. Fourth item\n";

    private static final String defaultMdRN = "# heading h1\r\n" + "## heading h2\r\n" + "### heading h2\r\n"
            + "#### heading h4\r\n" + "---\r\n" + "- 1st  list\r\n" + "    - second list\r\n"
            + "        - third list\r\n" + "- end list \r\n" + "---\r\n"
            + "[<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">](https://www.mozilla.org/)  \r\n"
            + "[![Foo](https://mdn.github.io/beginner-html-site/images/firefox-icon.png)](https://www.mozilla.org/)  \r\n"
            + "[Link to mozilla](https://www.mozilla.org/)\r\n" + "I just love **bold text**.  \r\n"
            + "I just love __bold text__.  \r\n" + "Love**is**bold  \r\n" + "A*cat*meow  \r\n" + "1. First item\r\n"
            + "3. Second item\r\n" + "8. Third item\r\n" + "    1. Indented item\r\n" + "    9. Indented item\r\n"
            + "10. Fourth item\n";

    private static final String defaultHTML = "<h1>heading h1</h1>\n" + "<h2>heading h2</h2>\n"
            + "<h3>heading h2</h3>\n" + "<h4>heading h4</h4>\n" + "<hr />\n" + "<ul>\n" + "<li>1st  list\n" + "<ul>\n"
            + "<li>second list\n" + "<ul>\n" + "<li>third list</li>\n" + "</ul>\n" + "</li>\n" + "</ul>\n" + "</li>\n"
            + "<li>end list</li>\n" + "</ul>\n" + "<hr />\n" + "<p><a href=\"https://www.mozilla.org/\">"
            + "<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">" + "</a><br />\n"
            + "<a href=\"https://www.mozilla.org/\">"
            + "<img src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\" "
            + "alt=\"Foo\" /></a><br />\n" + "<a href=\"https://www.mozilla.org/\">Link to mozilla</a><br />\n"
            + "I just love <strong>bold text</strong>.<br />\n" + "I just love <strong>bold text</strong>.<br />\n"
            + "Love<strong>is</strong>bold<br />\n" + "A<em>cat</em>meow</p>\n" + "<ol>\n" + "<li>First item</li>\n"
            + "<li>Second item</li>\n" + "<li>Third item\n" + "<ol>\n" + "<li>Indented item</li>\n"
            + "<li>Indented item</li>\n" + "</ol>\n" + "</li>\n" + "<li>Fourth item</li>\n" + "</ol>\n";

    /**
     * Test conversion markdown to HTML
     */
    @Test
    void convertMarkdownToHTML() {
        String convertedMd = HtmlConvertor.fromMarkdown(defaultMdN);
        assertEquals(defaultHTML, convertedMd, "Conversion markdown to HTML failed with \\n");

        convertedMd = HtmlConvertor.fromMarkdown(defaultMdRN);
        assertEquals(defaultHTML, convertedMd, "Conversion markdown to HTML failed with \\r\\n");
    }
}
