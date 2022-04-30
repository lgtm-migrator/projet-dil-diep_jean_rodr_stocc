package ch.heigvd.statique.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    private final Path site;
    private final int port;

    /**
     * Server constructor
     *
     * @param site path to site
     * @param port server port number
     */
    public Server(Path site, int port) {
        this.site = site;
        this.port = port;
    }

    /**
     * Start the server
     *
     * @throws IOException Couldn't start the server
     */
    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/", new MyHttpHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
        LOG.info(" Server started on port 1234");
    }

    class MyHttpHandler implements HttpHandler {

        /**
         * Handle http request
         * @param t http exchange
         * @throws IOException Couldn't respond to the client request
         */
        public void handle(HttpExchange t) throws IOException {
            // Modified code from : http://www.microhowto.info/howto/serve_web_pages_using_an_embedded_http_server_in_java.html

            // Gets request
            URI uri = t.getRequestURI();
            File file = new File(site + uri.getPath()).getCanonicalFile();

            LOG.info("File path : " + file.getPath());

            if(uri.toString().contains("/../")){
                // Suspected path traversal attack: reject with 403 error.
                String response = "403 (Forbidden)\n";
                t.sendResponseHeaders(403, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                LOG.info("Server : " + response);
            } else if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                LOG.info("Server : " + response);
            } else {
                // Object exists and is a file: accept with response code 200.
                t.sendResponseHeaders(200, 0);

                // Get page text
                String pageText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

                // Send page content
                try (BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(t.getResponseBody(), StandardCharsets.UTF_8))) {
                    out.write(pageText);
                    out.flush();
                }

                LOG.info("Server : file " + file.getName() + " sent");
            }
        }
    }
}
