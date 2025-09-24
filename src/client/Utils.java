package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Utils {
    public static byte[] readFile(File f) throws IOException {
        return Files.readAllBytes(f.toPath());
    }
}
