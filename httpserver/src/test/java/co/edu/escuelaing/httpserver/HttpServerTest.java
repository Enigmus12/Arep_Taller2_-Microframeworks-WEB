package co.edu.escuelaing.httpserver;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private static HttpServer server;
    private static Thread serverThread;

    @BeforeAll
    static void startServer() {
        server = new HttpServer();
        server.get("/hello", (req, res) -> "Hello " + req.getQueryParam("name"));

        serverThread = new Thread(() -> {
            try {
                server.start(8081); // usar puerto distinto al 8080
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private String doGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    @Test
    void testHelloEndpoint() throws Exception {
        String response = doGet("http://localhost:8081/hello?name=Juan");
        assertTrue(response.contains("Hello Juan"));
    }

    @Test
    void testNotFoundEndpoint() throws Exception {
        URL url = new URL("http://localhost:8081/notfound");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        assertEquals(200, conn.getResponseCode()); // aún devuelve 200 pero debe contener 404
        String body = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        assertTrue(body.contains("404"));
    }
}
