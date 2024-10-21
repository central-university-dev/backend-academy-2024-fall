package backend.academy.seminar6.wap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamWordsCalculator implements WordsCalculator {

    @Override
    public String getResult(Path... paths) {
        Stream<String>[] lineStreams = doStreamPaths(Charset.forName("cp1251"), paths);
        try {
            return Stream.of(lineStreams)
                .flatMap(Function.identity())
                .map(String::toLowerCase)
                .map(s -> s.split("[^a-zа-я]+"))
                .flatMap(Arrays::stream)
                .filter(s -> s.length() >= 4)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 10)
                .sorted(Comparator.<Entry<String, Long>>comparingLong(Entry::getValue)
                    .reversed()
                    .thenComparing(Entry::getKey))
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));
        } finally {
            for (var stream : lineStreams) {
                stream.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Stream<String>[] doStreamPaths(Charset charset, Path... paths) {
        return Arrays.stream(paths)
            .map(path -> {
                try {
                    return Files.lines(path, charset);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })
            .toArray(Stream[]::new);
    }
}
