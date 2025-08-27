package co.edu.escuelaing.httpserver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void testDefaultContentType() {
        HttpResponse res = new HttpResponse();
        assertEquals("text/html", res.getType());
    }

    @Test
    void testChangeContentType() {
        HttpResponse res = new HttpResponse();
        res.type("application/json");
        assertEquals("application/json", res.getType());
    }
}
