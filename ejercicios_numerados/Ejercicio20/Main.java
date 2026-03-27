import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Main {
    static Map<String, String> invoices = Map.of("1", "alice:50", "2", "bob:90");

    public static void main(String[] args) throws Exception {
        HttpServer internal = HttpServer.create(new InetSocketAddress("127.0.0.1", 9192), 0);
        internal.createContext("/internal", ex -> write(ex, "java-internal"));
        internal.start();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/invoice", Main::invoice);
        server.createContext("/jump", Main::jump);
        server.createContext("/fetch", Main::fetch);
        server.start();
    }

    static void invoice(HttpExchange ex) throws IOException {
        write(ex, invoices.getOrDefault(param(ex, "id"), "missing"));
    }

    static void jump(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Location", param(ex, "next"));
        ex.sendResponseHeaders(302, -1);
    }

    static void fetch(HttpExchange ex) throws IOException {
        String body = new String(new URL(param(ex, "url")).openStream().readAllBytes(), StandardCharsets.UTF_8);
        write(ex, body);
    }

    static String param(HttpExchange ex, String key) {
        String q = ex.getRequestURI().getRawQuery();
        if (q == null) return "";
        for (String p : q.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
        }
        return "";
    }

    static void write(HttpExchange ex, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
