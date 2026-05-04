import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/user", Main::user);
        server.createContext("/xml", Main::xml);
        server.createContext("/xpath", Main::xpath);
        server.start();
    }

    static void user(HttpExchange ex) throws IOException {
        write(ex, "SELECT * FROM users WHERE id = " + param(ex, "id"));
    }

    static void xml(HttpExchange ex) throws IOException {
        try {
            var body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            var dbf = DocumentBuilderFactory.newInstance();
            var doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(body)));
            write(ex, doc.getDocumentElement().getTextContent());
        } catch (Exception e) {
            write(ex, e.toString());
        }
    }

    static void xpath(HttpExchange ex) throws IOException {
        try {
            String xml = "<users><user><name>alice</name></user><user><name>bob</name></user></users>";
            String expr = "/users/user[name/text()='" + param(ex, "user") + "']/name/text()";
            XPath xp = XPathFactory.newInstance().newXPath();
            write(ex, xp.evaluate(expr, new InputSource(new StringReader(xml))));
        } catch (Exception e) {
            write(ex, e.toString());
        }
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
