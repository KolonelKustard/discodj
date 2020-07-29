package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.MediaEntity;
import com.totalchange.discodj.test.utils.CreateExampleMp3MediaFiles;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FileMediaSourceAllMediaTest {
    private static Path root;
    private static List<Path> testMediaFiles;

    @BeforeClass
    public static void beforeClass() throws IOException {
        root = CreateExampleMp3MediaFiles.createExampleMediaInTarget();
        testMediaFiles = Files
                .walk(root)
                .filter(path -> Files.isRegularFile(path))
                .collect(Collectors.toList());
    }

    @Test
    public void correctlyFindsAllTestFileMedia() throws IOException, ExecutionException, InterruptedException {
        final FileMediaSource fms = new FileMediaSource(root);
        final List<MediaEntity> all = fms.getAllMediaEntities().get();

        assertEquals("Expected " + testMediaFiles.size() + " media items in " + all, testMediaFiles.size(),
                all.size());
        for (final Path mf : testMediaFiles) {
            final MediaEntity me = findMediaEntityWithId(all, mf.toString());

            assertEquals("Modified timestamps should match for " + mf + " and " + me,
                    Files.getLastModifiedTime(mf).toMillis(), me.getLastModifiedMs());
        }
    }

    private MediaEntity findMediaEntityWithId(final List<MediaEntity> all, final String id) {
        final Optional<MediaEntity> match = all.stream().filter((me) -> me.getId().equals(id)).findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new RuntimeException("Couldn't find MediaEntity with id " + id + " in " + all);
        }
    }
}
