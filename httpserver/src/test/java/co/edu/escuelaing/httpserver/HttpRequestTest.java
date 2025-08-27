package co.edu.escuelaing.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void testGetPathWithoutQuery() {
        HttpRequest req = new HttpRequest("/hello");
        assertEquals("/hello", req.getPath());
    }

    @Test
    void testGetPathWithQuery() {
        HttpRequest req = new HttpRequest("/hello?name=Juan");
        assertEquals("/hello", req.getPath());
    }

    @Test
    void testQueryParamDecoding() {
        HttpRequest req = new HttpRequest("/hello?name=Juan+David&age=20");
        assertEquals("Juan David", req.getQueryParam("name")); // debe decodificar +
        assertEquals("20", req.getQueryParam("age"));
    }

    @Test
    void testQueryParamWithEmptyValue() {
        HttpRequest req = new HttpRequest("/hello?empty");
        assertEquals("", req.getQueryParam("empty"));
    }

    @Test
    void testGetQueryParamNotFound() {
        HttpRequest req = new HttpRequest("/hello");
        assertNull(req.getQueryParam("missing"));
    }
}
