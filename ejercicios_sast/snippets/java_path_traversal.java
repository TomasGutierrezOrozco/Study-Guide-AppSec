import java.nio.file.Files;
import java.nio.file.Path;

class Reader {
    public String read(String filename) throws Exception {
        Path base = Path.of("/srv/docs");
        return Files.readString(base.resolve(filename));
    }
}
