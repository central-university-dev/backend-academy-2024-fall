package backend.academy.seminar6.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class IOExample {

    private static final Logger logger = LoggerFactory.getLogger(IOExample.class);

    public static void main(String[] args) throws Exception {
        fileExample();
        outputStreamExample();
        inputStreamExample();
        writerExample();
        readerExample();
        pathExample();
        nioFilesExample();
        nioFilesAnotherExample();
        fileVisitorExample();
//        fileWatcherExample(); // will block forever, all modifications inside seminar6/src/main/resources will be logged
    }

    private static void fileExample() {
        File file = new File("seminar6/src/main/resources/example.txt");
        if (file.exists()) { // You can create an instance of a non-existing file
            logger.info("File {} exists!", file); // ./seminar6/src/main/resources/example.txt
            logger.info("File size: {} bytes", file.length());
            logger.info("Absolute path: {}", file.getAbsolutePath());
            logger.info("Writable: {}", file.canWrite());
            logger.info("Readable: {}", file.canRead());
        } else {
            logger.info("File {} does not exist!", file);
        }
    }

    private static void outputStreamExample() {
        // Will create file if it doesn't exist,
        // But will throw exception if any of the upper directories doesn't exist
        try (OutputStream outputStream = new FileOutputStream("seminar6/src/main/resources/example.txt")) {
            String content = "123456";
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void inputStreamExample() {
        // If file doesn't exist - throws exception!
        try (InputStream inputStream = new FileInputStream("seminar6/src/main/resources/example.txt")) {
            String content = new String(inputStream.readAllBytes());
            logger.info("Content: {}", content); // 123456
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void writerExample() {
        // Will create file if it doesn't exist,
        // But will throw exception if any of the upper directories doesn't exist
        try (Writer writer = new FileWriter("seminar6/src/main/resources/example.txt")) {
            String content = "123456";
            writer.write(content); // no byte conversion needed
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void readerExample() {
        // If file doesn't exist - throws exception!
        try (Reader reader = new FileReader("seminar6/src/main/resources/example.txt")) {
            char[] buffer = new char[1024];
            int result = reader.read(buffer); // no method to read all chars into String
            logger.info("Result: {}, content: {}", result, buffer); // Result: 6, content: [1, 2, 3, 4, 5, 6, ...]
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static String noTryWithResourcesExample() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("not.existing.file");
            byte[] result = inputStream.readAllBytes();
            return new String(result);
        } catch (IOException e) {
            logger.error("Exception while reading file", e);
            return null;
        } finally {
            // If inputStream == null, then we couldn't even open a file
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Could not close stream", e);
                }
            }
        }
    }

    private static void pathExample() {
        Path path = Path.of("seminar6", "src", "main", "resources", "example.txt");

        logger.info("Path: {}", path); // seminar6/src/main/resources/example.txt
        logger.info("File name: {}", path.getFileName()); // example.txt
        logger.info("Parent: {}", path.getParent()); // seminar6/src/main/resources
        logger.info("Root: {}", path.getRoot()); // null, path constructed without root

        // Convert path to absolute path
        logger.info("Absolute path: {}", path.toAbsolutePath()); // /Users/.../example.txt

        // Resolve another path
        Path resolvedPath = path.resolveSibling("log4j2.xml");
        logger.info("Resolved path: {}", resolvedPath); // seminar6/src/main/resources/log4j2.xml

        // Relativize between two paths
        Path otherPath = Paths.get(".mvn");
        logger.info("Relative path: {}", path.relativize(otherPath)); // ../../../../../.mvn
    }

    private static void nioFilesExample() {
        Path path = Path.of("seminar6", "src", "main", "resources", "example.txt");

        try {
            String content = "123456";
            Files.write(path, content.getBytes(), WRITE, CREATE);
            // or Files.newBufferedReader(path);
            // or Files.newInputStream(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            String content = Files.readString(path);
            // or Files.newBufferedWriter(path);
            // or Files.newOutputStream(path)
            logger.info("Content: {}", content); // 123456
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void nioFilesAnotherExample() throws IOException {
        Path path = Path.of("seminar6", "src", "main", "resources", "example.txt");

        Path copy = Files.copy(
            path,
            path.resolveSibling("copy.txt"),
            StandardCopyOption.REPLACE_EXISTING
        );
        logger.info("Copy: {}", copy); // seminar6/src/main/resources/copy.txt

        var fileSize = Files.size(path);
        var copySize = Files.size(copy);
        logger.info("Original: {}, copy: {}", fileSize, copySize); /// Original: 6, copy: 6

        Path currentDir = Path.of(".");
        try (
            Stream<Path> files = Files.find(currentDir, 6,
                (file, attrs) -> attrs.isRegularFile() && file.toString().endsWith("resources/copy.txt"))
        ) {
            files.forEach(file -> logger.info("Found: {}", file)); // ./seminar6/src/main/resources/copy.txt
        }

        Files.delete(copy);
        logger.info("Copy exists: {}", Files.exists(copy)); // false
    }

    private static void fileVisitorExample() throws IOException {
        try (Stream<Path> files = Files.walk(Path.of("."), FileVisitOption.FOLLOW_LINKS)) {
            files.filter(file -> file.endsWith("example.txt"))
                .forEach(file -> logger.info("Walk file: {}", file));
        }

        Files.walkFileTree(Path.of("."), new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (!dir.equals(Path.of(".")) && !dir.toAbsolutePath().toString().contains("seminar6")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.endsWith("example.txt")) {
                    logger.info("Visit file: {}", file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.SKIP_SIBLINGS;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void fileWatcherExample() throws Exception {
        Path path = Path.of("seminar6", "src", "main", "resources");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE
            );

            logger.info("Starting to watch...");
            while (true) {
                WatchKey watchKey;
                try {
                    watchKey = watchService.take();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw e;
                }

                for (var event : watchKey.pollEvents()) {
                    logger.info("Event kind: {}, context: {}", event.kind(), event.context());
                }

                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }
            }
            logger.info("Finished watching");
        }
    }
}
