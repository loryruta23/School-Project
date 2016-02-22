package it.mo.fermi.unnamed;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import sun.plugin.dom.DOMObject;
import sun.plugin.dom.html.HTMLButtonElement;
import sun.plugin.dom.html.HTMLDocument;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MyTest {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(command -> {
            command.run();
            System.out.println("Ta faq?");
        }); // creates a default executor
        server.start();
        System.out.println("Starting server!");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println("Handle...");
            String response = "<button type=\"button\">non riesco a captare il click dell'user :(</button>";

            HTMLButtonElement a = new HTMLButtonElement();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
