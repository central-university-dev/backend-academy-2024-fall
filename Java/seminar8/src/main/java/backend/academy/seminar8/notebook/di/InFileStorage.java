package backend.academy.seminar8.notebook.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InFileStorage implements Storage {
    private static final Logger logger = LoggerFactory.getLogger(InFileStorage.class);

    private final Cache<String, Notebook> cache;

    private final Path notebooksFolder;
    private final ObjectMapper objectMapper;

    @Inject
    public InFileStorage(Path notebooksFolder, ObjectMapper objectMapper, Cache<String, Notebook> cache) {
        this.notebooksFolder = notebooksFolder;
        this.objectMapper = objectMapper;
        this.cache = cache;
    }

    @Override
    public Optional<Notebook> findNotebookByName(String notebookName) {
        return Optional.ofNullable(cache.get(notebookName, k -> findNotebookByNameFromFile(k).orElse(null)));
    }

    private Optional<Notebook> findNotebookByNameFromFile(String notebookName) {
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

    @Override
    public Notebook saveNotebook(Notebook notebook) {
        Path notebookFile = notebooksFolder.resolve(notebook.getName() + ".json");
        try {
            if (!Files.exists(notebooksFolder)) {
                Files.createDirectories(notebooksFolder);
            }
            objectMapper.writeValue(notebookFile.toFile(), notebook);
            cache.put(notebook.getName(), notebook);
            return notebook;
        } catch (IOException e) {
            logger.error("Failed to save notebook", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> searchNotes(String query) {
        return cache.asMap().values()
            .stream()
            .map(Notebook::getNotes)
            .flatMap(Collection::stream)
            .filter(it -> it.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                it.getContent().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }
}
