package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.Media;
import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.server.media.MediaSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMediaSource implements MediaSource {
    private static final Logger logger = LoggerFactory.getLogger(FileMediaSource.class);

    private final ExecutorService executor = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(),
            20L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("file-media"));
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
                        .filter(this::filterOutJustSupportedMediaFiles)
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
    public CompletableFuture<Media> getMedia(final String id) {
        final CompletableFuture<Media> cf = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                final Path pathToMedia = Paths.get(id).toRealPath();
                cf.complete(new FileMedia(root.toString(), pathToMedia));
            } catch (Exception ex) {
                logger.error("Failed trying to fetch media with id {}", id, ex);
                cf.completeExceptionally(ex);
            }
        });
        return cf;
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

    private boolean filterOutJustSupportedMediaFiles(final Path path) {
        return Files.isRegularFile(path) &&
                Files.isReadable(path) &&
                path.toString().endsWith(".mp3");
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
