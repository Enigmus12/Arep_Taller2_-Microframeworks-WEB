package co.edu.escuelaing.httpserver;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private String staticFolder = "www";
    private Map<String, Route> getRoutes = new HashMap<>();

    @FunctionalInterface
    public interface Route {
        String handle(HttpRequest req, HttpResponse res);
    }

    public void staticfiles(String folder) {
        staticFolder = folder;
    }

    public void get(String path, Route route) {
        getRoutes.put(path, route);
    }

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en http://localhost:" + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleRequest(clientSocket);
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true)
        ) {
            String inputLine = in.readLine();
            if (inputLine == null) return;
            System.out.println("Solicitud: " + inputLine);

            String[] requestParts = inputLine.split(" ");
            String path = requestParts[1];
            HttpRequest req = new HttpRequest(path);
            HttpResponse res = new HttpResponse();

            if (getRoutes.containsKey(req.getPath())) {
                String result = getRoutes.get(req.getPath()).handle(req, res);
                sendResponse(writer, res.getType(), result);
            } else {
                if (path.equals("/")) path = "/index.html";
                File file = new File(staticFolder + path);
                if (file.exists() && !file.isDirectory()) {
                    String mimeType = Files.probeContentType(file.toPath());
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    sendFileResponse(out, writer, mimeType, fileContent);
                } else {
                    String notFound = "<h1>404 Not Found</h1>";
                    sendResponse(writer, "text/html", notFound);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(PrintWriter writer, String contentType, String response) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + contentType + "; charset=utf-8");
        writer.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);
        writer.println();
        writer.println(response);
    }

    private void sendFileResponse(OutputStream out, PrintWriter writer, String mimeType, byte[] fileContent) throws IOException {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + mimeType);
        writer.println("Content-Length: " + fileContent.length);
        writer.println();
        writer.flush();
        out.write(fileContent);
        out.flush();
    }
}
