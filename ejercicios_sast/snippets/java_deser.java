import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class Importer {
    public Object load(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return in.readObject();
    }
}
