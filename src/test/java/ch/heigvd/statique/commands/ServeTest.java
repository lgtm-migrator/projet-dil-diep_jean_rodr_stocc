package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServeTest {
  static Path root;
  private final static int PORT = 12341;
  private final static LinkedList<String> uriLinks = new LinkedList<>();

  /**
   * Creates environment for test
   *
   * @throws RuntimeException File creation exception
   */
  @BeforeAll
  static void createEnvironment() throws RuntimeException, IOException {
    root = Files.createTempDirectory("site");
    // Initialize site
    int exitCode = new CommandLine(new Init()).execute(String.valueOf(root));
    if(exitCode == -1)
      throw new RuntimeException("Couldn't init site");

    // Build site
    exitCode = new CommandLine(new Build()).execute(String.valueOf(root));
    if(exitCode == -1)
      throw new RuntimeException("Couldn't build site");

    // Add links
    uriLinks.add("/index.html");

    // Create server
    new CommandLine(new Serve()).execute(String.valueOf(root), String.valueOf(PORT));
  }

  /**
   * Clean up temporary directory
   */
  @AfterAll
  static void tearDown() throws IOException {
    Utils.deleteRecursive(root);
  }

  /**
   * @return Simple HTTP client
   */
  private HttpClient getHttpClient(){
    return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .proxy(ProxySelector.of(new InetSocketAddress("localhost", PORT)))
            .build();
  }

  /**
   * Test serve command
   */
  @Test
  void getResponse() throws IOException, InterruptedException {
    HttpClient client = getHttpClient();

    // Checks could get links
    for(String uriLink : uriLinks){
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:" + PORT + uriLink))
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      assertEquals(200, response.statusCode());
    }
  }

  /**
   * Test 404 status code
   */
  @Test
  void get404() throws IOException, InterruptedException {
    HttpClient client = getHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + PORT + "/eweewf.ewfn"))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(404, response.statusCode());
  }

  /**
   * Test 403 status code
   */
  @Test
  void get403() throws IOException, InterruptedException {
    HttpClient client = getHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + PORT + "/../../admincode.txt"))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(403, response.statusCode());
  }
}
