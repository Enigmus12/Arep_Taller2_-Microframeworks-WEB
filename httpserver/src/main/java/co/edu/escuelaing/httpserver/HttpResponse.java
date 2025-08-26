package co.edu.escuelaing.httpserver;

public class HttpResponse {
    private String type = "text/html";

    public void type(String mimeType) { this.type = mimeType; }
    public String getType() { return type; }
}
