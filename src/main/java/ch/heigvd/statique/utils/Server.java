package ch.heigvd.statique.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    private final Path site;

    public Server(Path site) {
        this.site = site;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(1234), 1);
        server.createContext("/", new MyHttpHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
        LOG.info(" Server started on port 1234");
    }



    class MyHttpHandler implements HttpHandler {

        public void handle(HttpExchange t) throws IOException {
            String root = site + "/build";
            URI uri = t.getRequestURI();
            File file = new File(root + uri.getPath()).getCanonicalFile();

            LOG.info("File path : " + file.getPath());

            if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Object exists and is a file: accept with response code 200.
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer,0,count);
                }
                fs.close();
                os.close();
            }
        }
    }
}
