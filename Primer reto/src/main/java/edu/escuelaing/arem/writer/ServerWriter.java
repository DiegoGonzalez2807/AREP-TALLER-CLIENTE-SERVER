package edu.escuelaing.arem.writer;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerWriter {

    static String file, extensionFile,outputLine;
    //Lista de extensiones de texto
    static List<String> Extension_text = new ArrayList<String>(
            Arrays.asList(".html",".js",".css")
    );
    //Lista de extensiones de imagenes
    static List<String> Extension_Image = new ArrayList<String>(
            Arrays.asList(".png",".jpg")
    );

    static Socket client;


    /**
     * Funcion generada para revisar cual recurso está pidiendo el usuario
     * de acuerdo a la extension de este
     * @param path
     */
    public static void writer(String path, Socket clientSocket) throws IOException {
        file = path;
        client = clientSocket;
        System.out.println(file);
        if(Extension_text.stream().anyMatch(extension -> file.contains(extension))){
            extensionFile = file.split("\\.")[1];
            System.out.println("EXTENSION "+extensionFile);
            writeText();
        }
        else if(Extension_Image.stream().anyMatch(extension -> file.contains(extension))){
            extensionFile = file.split("\\.")[1];
            writeImage();
        }
        else{
            System.out.println("No se puede leer el recurso con ese formato");
        }
    }

    /**
     * Funcion generada para leer el contenido de la pagina html que este buscando el
     * usuario de acuerdo con el path ingresado
     */
    public static void writeText() {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n";
            outputLine += "Content-Type: text/" + extensionFile + "\r\n";
            outputLine += "\r\n";
            outputLine+= new String(Files.readAllBytes(Paths.get("resources/public"+file)));
            out.println(outputLine);
            out.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer el texto pedido");
            e.printStackTrace();
        }

    }


    /**
     * Funcion generada para leer el contenido de la imagen para que esta pueda ser mostrada
     * en la pagina html.
     */
    public static void writeImage() throws IOException {
        File inserted_image = new File("resources/public"+file);
        FileInputStream fis = new FileInputStream(inserted_image);
        byte[] data = new byte[(int) inserted_image.length()];
        fis.read(data);
        fis.close();
        DataOutputStream binaryOut = new DataOutputStream(client.getOutputStream());
        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
        binaryOut.writeBytes("Content-Type: image/"+extensionFile+"\r\n");
        binaryOut.writeBytes("Content-Length: " + data.length);
        binaryOut.writeBytes("\r\n\r\n");
        binaryOut.write(data);
        binaryOut.close();
    }
}
