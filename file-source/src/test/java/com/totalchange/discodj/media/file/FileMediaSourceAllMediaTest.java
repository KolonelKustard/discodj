package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.MediaEntity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class FileMediaSourceAllMediaTest {
    private static final int NUM_TEST_ARTISTS = 10;
    private static final int NUM_TEST_ALBUMS = 10;
    private static final int NUM_TEST_TRACKS = 10;

    private static Path root;
    private static List<Path> testMediaFiles;

    @BeforeClass
    public static void setUp() throws IOException {
        createTemporaryFiles();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        deleteTemporaryFiles();
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

    private static void createTemporaryFiles() throws IOException {
        root = Files.createTempDirectory("discodj-file-media-source-tests").toRealPath();
        testMediaFiles = new ArrayList<>();

        for (int artistNum = 0; artistNum < NUM_TEST_ARTISTS; artistNum++) {
            final Path artistPath = Files.createDirectory(
                    root.resolve(String.format("Test-Artist-%02d", artistNum)));

            for (int albumNum = 0; albumNum < NUM_TEST_ALBUMS; albumNum++) {
                final Path albumPath = Files.createDirectory(
                        artistPath.resolve(String.format("Test-Album-%02d", albumNum)));

                for (int trackNum = 0; trackNum < NUM_TEST_TRACKS; trackNum++) {
                    final Path trackPath = albumPath.resolve(String.format("Test-Track-%02d.mp3", trackNum));
                    testMediaFiles.add(Files.createFile(trackPath));
                }

                Files.createFile(albumPath.resolve("red-herring.wav"));
            }
        }
    }

    private static void deleteTemporaryFiles() throws IOException {
        Files.walk(root).sorted(Comparator.reverseOrder()).forEach((path) -> {
            try {
                Files.delete(path);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to delete file " + path + " from temporary location " + root, ex);
            }
        });
    }
}
