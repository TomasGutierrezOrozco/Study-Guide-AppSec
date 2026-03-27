import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/xml", Main::xml);
        server.createContext("/deserialize", Main::deserialize);
        server.createContext("/bind", Main::bind);
        server.start();
    }

    static void xml(HttpExchange ex) throws IOException {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(body)));
            write(ex, doc.getDocumentElement().getTextContent());
        } catch (Exception e) {
            write(ex, e.toString());
        }
    }

    static void deserialize(HttpExchange ex) throws IOException {
        try {
            byte[] data = java.util.Base64.getDecoder().decode(param(ex, "data"));
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
            write(ex, String.valueOf(in.readObject()));
        } catch (Exception e) {
            write(ex, e.toString());
        }
    }

    static void bind(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> account = new HashMap<>();
        account.put("role", "user");
        for (String part : body.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2) {
                account.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        write(ex, account.toString());
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
