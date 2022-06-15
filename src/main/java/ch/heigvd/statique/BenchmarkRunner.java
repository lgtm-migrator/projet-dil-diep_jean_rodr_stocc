package ch.heigvd.statique;

import ch.heigvd.statique.convertors.HtmlConvertor;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BenchmarkRunner {

    @Param({"1", "10", "100", "1000"})
    public int num;

    /**
     * From the command line: $ mvn clean install $ java -jar target/benchmarks.jar BenchmarkRunner
     *
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options opt =
                new OptionsBuilder()
                        .include(BenchmarkRunner.class.getSimpleName())
                        .forks(0)
                        .mode(Mode.SingleShotTime)
                        .warmupIterations(5)
                        .measurementIterations(5)
                        .forks(1)
                        .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void markdownRender() {
        for (int i = 0; i < num; i++) {
            String mark =
                    "# heading h1\n"
                        + "## heading h2\n"
                        + "### heading h2\n"
                        + "#### heading h4\n"
                        + "---\n"
                        + "- 1st  list\n"
                        + "    - second list\n"
                        + "        - third list\n"
                        + "- end list \n"
                        + "---\n"
                        + "[<img"
                        + " src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">](https://www.mozilla.org/)"
                        + "  \n"
                        + "[![Foo](https://mdn.github.io/beginner-html-site/images/firefox-icon.png)](https://www.mozilla.org/)"
                        + "  \n"
                        + "[Link to mozilla](https://www.mozilla.org/)\n"
                        + "I just love **bold text**.  \n"
                        + "I just love __bold text__.  \n"
                        + "Love**is**bold  \n"
                        + "A*cat*meow  \n"
                        + "1. First item\n"
                        + "3. Second item\n"
                        + "8. Third item\n"
                        + "    1. Indented item\n"
                        + "    9. Indented item\n"
                        + "10. Fourth item\n";
            HtmlConvertor.fromMarkdown(mark);
        }
    }

    @Benchmark
    public void markdownRenderOptimized() {
        Parser parser = Parser.builder().build();
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        for (int i = 0; i < num; i++) {
            String mark =
                    "# heading h1\n"
                        + "## heading h2\n"
                        + "### heading h2\n"
                        + "#### heading h4\n"
                        + "---\n"
                        + "- 1st  list\n"
                        + "    - second list\n"
                        + "        - third list\n"
                        + "- end list \n"
                        + "---\n"
                        + "[<img"
                        + " src=\"https://mdn.github.io/beginner-html-site/images/firefox-icon.png\">](https://www.mozilla.org/)"
                        + "  \n"
                        + "[![Foo](https://mdn.github.io/beginner-html-site/images/firefox-icon.png)](https://www.mozilla.org/)"
                        + "  \n"
                        + "[Link to mozilla](https://www.mozilla.org/)\n"
                        + "I just love **bold text**.  \n"
                        + "I just love __bold text__.  \n"
                        + "Love**is**bold  \n"
                        + "A*cat*meow  \n"
                        + "1. First item\n"
                        + "3. Second item\n"
                        + "8. Third item\n"
                        + "    1. Indented item\n"
                        + "    9. Indented item\n"
                        + "10. Fourth item\n";
            Node document = parser.parse(mark);
            htmlRenderer.render(document);
        }
    }
}
