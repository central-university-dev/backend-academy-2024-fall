package backend.academy.seminar8.notebook.nodi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InFileStorage {
    private static final Logger logger = LoggerFactory.getLogger(InFileStorage.class);

    // Жестко закодированный notebooksFolder
    private final Path notebooksFolder = Path.of("src", "main", "resources", "notebooks");

    // Жестко закодированный cache
    private final Cache<String, List<Note>> cache = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    // Жестко закодированный objectMapper
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public Optional<Notebook> findNotebookByName(String notebookName) {
        Path notebookFile = notebooksFolder.resolve(notebookName + ".json");
        if (Files.exists(notebookFile)) {
            try {
                Notebook notebook = objectMapper.readValue(notebookFile.toFile(), Notebook.class);
                return Optional.of(notebook);
            } catch (IOException e) {
                logger.error("Failed to read notebook", e);
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    public Notebook saveNotebook(Notebook notebook) {
        Path notebookFile = notebooksFolder.resolve(notebook.getName() + ".json");
        try {
            if (!Files.exists(notebooksFolder)) {
                Files.createDirectories(notebooksFolder);
            }
            objectMapper.writeValue(notebookFile.toFile(), notebook);
            cache.put(notebook.getName(), notebook.getNotes());
            return notebook;
        } catch (IOException e) {
            logger.error("Failed to save notebook", e);
            throw new RuntimeException(e);
        }
    }

    public List<Note> searchNotes(String query) {
        return cache.asMap().values()
            .stream()
            .flatMap(Collection::stream)
            .filter(it -> it.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                it.getContent().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }
}
