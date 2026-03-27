import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("/app/files"));
        Files.writeString(Path.of("/app/files/note.txt"), "public note", StandardCharsets.UTF_8);
        Files.writeString(Path.of("/app/secret.txt"), "java-secret", StandardCharsets.UTF_8);

        HttpServer internal = HttpServer.create(new InetSocketAddress("127.0.0.1", 9191), 0);
        internal.createContext("/admin", exchange -> write(exchange, "internal-java-admin"));
        internal.start();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> write(exchange, "Endpoints: /preview?url=http://example.com , /download?file=note.txt"));
        server.createContext("/preview", Main::preview);
        server.createContext("/download", Main::download);
        server.start();
    }

    static void preview(HttpExchange exchange) throws IOException {
        String target = query(exchange.getRequestURI(), "url");
        String body = new String(new URL(target).openStream().readAllBytes(), StandardCharsets.UTF_8);
        write(exchange, body);
    }

    static void download(HttpExchange exchange) throws IOException {
        String filename = query(exchange.getRequestURI(), "file");
        Path base = Path.of("/app/files");
        String body = Files.readString(base.resolve(filename), StandardCharsets.UTF_8);
        write(exchange, body);
    }

    static String query(URI uri, String key) {
        String raw = uri.getRawQuery() == null ? "" : uri.getRawQuery();
        for (String part : raw.split("&")) {
            String[] pieces = part.split("=", 2);
            if (pieces.length == 2 && pieces[0].equals(key)) {
                return java.net.URLDecoder.decode(pieces[1], StandardCharsets.UTF_8);
            }
        }
        return "";
    }

    static void write(HttpExchange exchange, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
