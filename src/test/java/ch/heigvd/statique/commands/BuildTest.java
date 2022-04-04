package ch.heigvd.statique.commands;

import ch.heigvd.statique.convertors.HtmlConvertor;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BuildTest {
    static Path root, build;
    private static final LinkedList<Path> filesMDPath = new LinkedList<>();
    private static final LinkedList<Path> filesHtmlPath = new LinkedList<>();
    private static final LinkedList<Path> filesYamlPath = new LinkedList<>();
    private static final LinkedList<Path> filesOtherPath = new LinkedList<>();
    private static final LinkedList<String> filesMDText = new LinkedList<>();
    private static final LinkedList<String> filesHtmlText = new LinkedList<>();
    private static final LinkedList<String> filesYamlText = new LinkedList<>();
    private static final LinkedList<Map<String, Object>> filesYamlMap = new LinkedList<>();


    /**
     * Writes inside a file
     *
     * @param filePath file path
     * @param text    file text (using \n separators)
     * @throws IOException BufferWritter exception
     */
    private static void writeFile(String filePath, String text) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8
        ))) {
            out.write(text);
        }
    }

    /**
     * Creates files for test
     *
     * @throws IOException File creation exception
     */
    @BeforeAll
    static void createFiles() throws IOException {
        root = Files.createTempDirectory("site");
        build = root.resolve("build");
        filesMDPath.add(Files.createFile(root.resolve("index.md")));
        filesMDText.add(
                "# Mon premier article\n" +
                "## Mon sous-titre\n" +
                "Le contenu de mon article.\n" +
                "![Une image](./image.png)"
        );
        filesHtmlText.add(
                "<h1>Mon premier article</h1>\n" +
                "<h2>Mon sous-titre</h2>\n" +
                "<p>Le contenu de mon article.</p>\n" +
                "<img src=\"./image.png\" alt=\"Une image\"/>"
        );
        writeFile(filesMDPath.getLast().toString(), filesMDText.getLast());
        filesMDPath.set(filesHtmlPath.size()-1, build.resolve("index.md"));

        filesYamlPath.add(Files.createFile(root.resolve("config.yaml")));
        filesYamlText.add(
                "titre: Mon premier article\n"+
                "auteur: Jean François\n"+
                "date: 2021-03-10\n"+
                "chiffre: 25\n"
        );
        filesYamlMap.addLast(new HashMap<>(){{
            put("titre", "Mon premier article");
            put("auteur", "Jean François");
            put("date", new Yaml().loadAs("2021-03-10", Date.class));
            put("chiffre", 25);
        }});
        writeFile(filesYamlPath.getLast().toString(), filesYamlText.getLast());
        filesMDPath.set(filesHtmlPath.size()-1, build.resolve("config.yaml"));

        filesMDPath.add(Files.createFile(root.resolve("dossier/page.md")));
        filesMDText.add(
                "# Première page\n"
        );
        filesHtmlText.add(
                "<h1>Première page</h1>\n"
        );
        writeFile(filesMDPath.getLast().toString(), filesMDText.getLast());
        filesMDPath.set(filesHtmlPath.size()-1, build.resolve("dossier/page.md"));

        filesOtherPath.add(root.resolve("dossier/image.png"));
        imageGenerator(filesOtherPath.getLast().toString());
        filesOtherPath.set(filesOtherPath.size()-1, build.resolve("dossier/image.png"));
    }

    /**
     * Generates an image
     * Code from :
     * https://dyclassroom.com/image-processing-project/how-to-create-a-random-pixel-image-in-java
     * @param path image path with name and png extension
     * @throws IOException
     */
    public static void imageGenerator(String path)throws IOException{
        //image dimension
        int width = 640;
        int height = 320;
        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;
        //create random image pixel by pixel
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int a = (int)(Math.random()*256); //alpha
                int r = (int)(Math.random()*256); //red
                int g = (int)(Math.random()*256); //green
                int b = (int)(Math.random()*256); //blue

                int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }
        //write image
        try{
            f = new File(path);
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }

    /**
     * Clean up temporary dir and file
     */
    @AfterEach
    void tearDown() throws IOException {
        Utils.deleteRecursive(root);
    }
}
