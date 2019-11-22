package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.Media;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class FileMediaTest {
    @Test
    public void readsAnId3V2Tag() throws IOException, ExecutionException, InterruptedException {
        final Path root = Paths.get("./src/test/resources/mp3-files").toRealPath();
        final Path mediaFile = Paths.get("./src/test/resources/mp3-files/sample-with-id3-v2-tag.mp3").toRealPath();

        final FileMediaSource fms = new FileMediaSource(root);
        final Media media = fms.getMedia(mediaFile.toString()).get();

        assertEquals(root.toString(), media.getSourceId());
        assertEquals(mediaFile.toString(), media.getId());
        assertEquals(Files.getLastModifiedTime(mediaFile).toMillis(), media.getLastModifiedMs());
        assertEquals("Test Artist", media.getArtist());
        assertEquals("Test Album", media.getAlbum());
        assertEquals("Test Genre", media.getGenre());
        assertEquals(1950, media.getYear());
        assertEquals("Test Title", media.getTitle());
        assertEquals(mediaFile.toUri(), media.getUri());
    }

    @Test
    public void readsAnId3V1Tag() throws IOException, ExecutionException, InterruptedException {
        final Path root = Paths.get("./src/test/resources/mp3-files").toRealPath();
        final Path mediaFile = Paths.get("./src/test/resources/mp3-files/sample-with-id3-v1-tag.mp3").toRealPath();

        final FileMediaSource fms = new FileMediaSource(root);
        final Media media = fms.getMedia(mediaFile.toString()).get();

        assertEquals(root.toString(), media.getSourceId());
        assertEquals(mediaFile.toString(), media.getId());
        assertEquals(Files.getLastModifiedTime(mediaFile).toMillis(), media.getLastModifiedMs());
        assertEquals("Test Artist", media.getArtist());
        assertEquals("Test Album", media.getAlbum());
        assertEquals("Electronic", media.getGenre());
        assertEquals(1950, media.getYear());
        assertEquals("Test Title", media.getTitle());
        assertEquals(mediaFile.toUri(), media.getUri());
    }

    @Test
    public void rejectsMp3WithoutId3Tags() throws IOException, InterruptedException {
        final Path root = Paths.get("./src/test/resources/mp3-files").toRealPath();
        final Path mediaFile = Paths.get("./src/test/resources/mp3-files/sample-without-id3-tags.mp3").toRealPath();

        final FileMediaSource fms = new FileMediaSource(root);
        assertExceptional(fms.getMedia(mediaFile.toString()), FileMediaException.class,
                "Media file " + mediaFile + " doesn't appear to have valid ID3 tags");
    }

    private void assertExceptional(final CompletableFuture<?> f, final Class clazz, final String msg) throws InterruptedException {
        try {
            f.get();
            fail("Expected to get an ExecutionException from this CompletableFuture");
        } catch (ExecutionException ex) {
            assertNotNull("Expected there to be a cause for the completable future failure", ex.getCause());
            assertEquals("Expected the cause of the completable future failure to be " + clazz, clazz,
                    ex.getCause().getClass());
            assertEquals("Expected an error message in the cause of " + msg, ex.getCause().getMessage(), msg);
        }
    }
}
