package example.nio.fs;

import java.io.IOException;
import java.util.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.*;

public class ZipFsDemo {

    public static void main(String[] args) throws Throwable {
        readJar();
//        copyFileIntoJar();
    }

    private static void readJar() throws IOException {
        URI url = URI.create("jar:file:///C:/Users/puppylpg/Codes/java-examples/target/java-examples-1.0-SNAPSHOT.jar!/");
        // Mount the zip file as a file system
        try (FileSystem zipFS = FileSystems.newFileSystem(url, Collections.emptyMap())) {
            Path zipPath = zipFS.getPath("/example");
            Files.list(zipPath).map(Path::toString).forEach(System.out::println);
        }
    }

    private static void copyFileIntoJar() throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        // locate file system by using the syntax
        // defined in java.net.JarURLConnection
        URI uri = URI.create("jar:file:/codeSamples/zipfs/zipfstest.zip");

        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            Path externalTxtFile = Paths.get("/codeSamples/zipfs/SomeTextFile.txt");
            Path pathInZipfile = zipfs.getPath("/SomeTextFile.txt");
            // copy a file into the zip file
            Files.copy(externalTxtFile, pathInZipfile, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}