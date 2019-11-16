package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.media.MediaSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMediaSource implements MediaSource {
    private static final Logger logger = LoggerFactory.getLogger(FileMediaSource.class);

    private final Executor executor = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(),
            20L, TimeUnit.SECONDS, new SynchronousQueue<>(), new NamedThreadFactory("file-media"));
    private final Path root;

    public FileMediaSource(final Path root) throws IOException {
        this.root = root.toRealPath();
    }

    @Override
    public String getId() {
        return root.toString();
    }

    @Override
    public CompletableFuture<List<MediaEntity>> getAllMediaEntities() {
        final CompletableFuture<List<MediaEntity>> cf = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                cf.complete(Files
                        .walk(root)
                        .flatMap(this::convertToMediaEntityWherePossible)
                        .collect(Collectors.toList()));
            } catch (IOException ex) {
                logger.error("Something went wrong fetching all media entities for {}", root, ex);
                cf.completeExceptionally(ex);
            }
        });
        return cf;
    }

    @Override
    public CompletableFuture<Media> getMedia(String id) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMediaSource that = (FileMediaSource) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    private Stream<MediaEntity> convertToMediaEntityWherePossible(final Path path) {
        try {
            return Stream.of(new FileMediaEntity(path));
        } catch (IOException ex) {
            logger.warn("Skipped over {} when listing all media for {}", path, root, ex);
            return Stream.empty();
        }
    }
}
