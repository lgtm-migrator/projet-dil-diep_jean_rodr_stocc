package ch.heigvd.statique.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
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
        Path configFile;
        build = site.resolve("build");

        // Delete directory of exists
        if (Files.exists(build)) {
            Utils.deleteRecursive(build);
        }

        Files.createDirectories(build);

        // Get the config file path
        if (Files.exists(site.resolve("config.yml"))) {
            configFile = site.resolve("config.yml");
        } else if (Files.exists(site.resolve("config.yaml"))) {
            configFile = site.resolve("config.yaml");
        } else {
            configFile = null;
        }

        // Read the config file
        if (configFile != null) {
            config = YamlConvertor.read(configFile.toString());
        } else {
            config = new HashMap<>();
        }

        exploreAndBuild(site.toFile(), build);

        return 0;
    }

    /**
     * Explores a directory and creates a build directory When the explorer sees
     * a directory, it recursively explores the directory and copies it When the
     * explorer sees a file, it converts the file or copy it in the build
     * directory.
     *
     * @param rootDirectory  root directory
     * @param buildDirectory build directory
     */
    private void exploreAndBuild(File rootDirectory, Path buildDestination) throws IOException {
        // Build the files
        if (rootDirectory.isFile()) {
            fileBuilding(rootDirectory, buildDestination);
            return;
        }

        // Must be a directory
        if (!rootDirectory.isDirectory()) {
            return;
        }

        // Must not be the build directory
        if (rootDirectory.toPath().equals(build)) {
            return;
        }

        Path buildDirectory = buildDestination;

        // Must not be the root directory
        if (!rootDirectory.toPath().equals(site)) {
            // Create the subdirectory in build directory
            buildDirectory = buildDestination.resolve(rootDirectory.getName());
            Files.createDirectories(buildDirectory);
        }

        // Call recursively for each subdirectory and files
        File[] files = rootDirectory.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; ++i) {
            exploreAndBuild(files[i], buildDirectory);
        }
    }

    /**
     * Builds the given file
     *
     * @param file        file to build
     * @param destination directory inside build folder
     */
    private void fileBuilding(File file, Path destination) throws IOException {
        if (file.getName().endsWith(".md")) {
            // Converts MD to HTML
            // Use $ in regex to match the end of the file name
            HtmlConvertor.createHtmlFileFromMarkdown(file.getPath(), destination + File.separator,
                    file.getName().replaceFirst("md$", "html"));
        } else if (!file.getName().endsWith(".yaml") && !file.getName().endsWith(".yml")) {
            // Copy path
            Files.copy(file.toPath(), destination.resolve(file.getName()));
        }
    }
}