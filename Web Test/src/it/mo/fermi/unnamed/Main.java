package it.mo.fermi.unnamed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    /**
     * WebServer constructor.
     */
    protected void start() {
        ServerSocket s;

        System.out.println("Webserver starting up on port 80");
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main server socket
            s = new ServerSocket(9090);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        while (true) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                // read the data sent. We basically ignore it,
                // stop reading once a blank line is hit. This
                // blank line signals the end of the client HTTP
                // headers.
                String str = ".";
                while (!str.equals(""))
                    str = in.readLine();

                // Send the response
                // Send the headers
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Bot");
                // this blank line signals the end of the headers
                out.println("");
                // Send the HTML page
                out.println("<button type=\"button\">non riesco a captare il click dell'user :(</button>");
                out.flush();

                remote.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    /**
     * Start the application.
     *
     * @param args
     *            Command line parameters are not used.
     */
    public static void main(String args[]) {
        Main ws = new Main();
        ws.start();
    }
}


