package com.totalchange.discodj.media.file;

import com.totalchange.discodj.server.media.Media;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class FileMediaTest {
    @Test
    public void readsAnId3Tag() throws IOException, ExecutionException, InterruptedException {
        final Path root = Paths.get("./src/test/resources/mp3-files").toRealPath();
        final Path mediaFile = Paths.get("./src/test/resources/mp3-files/silence.mp3").toRealPath();

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
}
