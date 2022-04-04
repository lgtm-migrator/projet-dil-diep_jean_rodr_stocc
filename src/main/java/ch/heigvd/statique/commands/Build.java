package ch.heigvd.statique.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.nio.file.Path;

import ch.heigvd.statique.convertors.HtmlConvertor;
import ch.heigvd.statique.convertors.YamlConvertor;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {
    @Parameters(paramLabel = "SITE", description = "The site to build")
    public Path site;
    private Path build;
    public static Map<String, Object> config;

    @Override
    public Integer call() throws IOException {
        build = site.resolve("build");
        // Delete directory of exists
        if(Files.exists(build)){
            Utils.deleteRecursive(build);
        }
        Files.createDirectories(build);

        exploreAndBuild(site.toFile());

        return 0;
    }

    /**
     * Explores a directory and creates a build directory
     * When the explorer sees a directory, it recursively explores the directory and copies it
     * When the explorer sees a file, it converts the file or copy it in the build directory.
     * @param rootDirectory root directory
     */
    private void exploreAndBuild(File rootDirectory) throws IOException {
        if(rootDirectory.isFile()){
            fileBuilding(rootDirectory, "");
        }
        else if (rootDirectory.isDirectory()){
            String directoryInBuild = rootDirectory.toPath().toString().substring(
                    rootDirectory.toPath().toString().lastIndexOf(File.separator)+1
            );
            if(directoryInBuild.equals(
                    site.toString().substring(site.toString().lastIndexOf(File.separator) + 1)
                ))
            {
                directoryInBuild = "";
            } else if(directoryInBuild.equals(build.toString().substring(build.toString().lastIndexOf(File.separator) + 1))){
                return;
            }
            else{
                Files.createDirectories(build.resolve(directoryInBuild));
            }


            File[] files = rootDirectory.listFiles();
            for(int i = 0; i < Objects.requireNonNull(files).length; ++i){
                if(files[i].isFile())
                    fileBuilding(files[i], directoryInBuild);
                else
                    exploreAndBuild(files[i]);
            }
        }

    }

    /**
     * Builds the given file
     * @param file file to build
     * @param directoryInBuild directory inside build folder
     */
    private void fileBuilding(File file, String directoryInBuild) throws IOException {
        if(file.getName().contains(".md")){
            // Converts MD to HTML
            HtmlConvertor.createHtmlFileFromMarkdown(
                    file.getPath(),
                    build.resolve(directoryInBuild) + File.separator,
                    file.getName().replaceFirst("\\.md", ".html")
            );
        } else if (file.getName().contains(".yaml") ||
                file.getName().contains(".yml")){
            // Gets YAML config
            config = YamlConvertor.read(file.getPath());
        } else {
            // Copy path
            Files.copy(file.toPath(), build.resolve(directoryInBuild).resolve(file.getName()));
        }
    }
}