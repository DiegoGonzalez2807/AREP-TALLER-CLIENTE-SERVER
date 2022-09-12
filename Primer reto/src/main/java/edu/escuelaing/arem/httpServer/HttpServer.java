package edu.escuelaing.arem.httpServer;

import edu.escuelaing.arem.writer.ServerWriter;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                ServerWriter serverWriter = new ServerWriter();
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, path ="";
            boolean flag = true;
            while ((inputLine = in.readLine()) != null) {
                if (flag) {
                    flag = false;
                    path = inputLine.split(" ")[1];
                    System.out.println("ESTE ES EL PATH"+path);
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            if(path.equals("/")){
                path = "/index.html";
            }
            ServerWriter.writer(path,clientSocket);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
 
    static int getPort() {
        if (System.getenv("PORT") != null) {
        return Integer.parseInt(System.getenv("PORT"));
    }
    return 4567;
 }
}
