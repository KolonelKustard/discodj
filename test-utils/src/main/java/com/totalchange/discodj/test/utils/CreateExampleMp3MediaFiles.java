package com.totalchange.discodj.test.utils;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateExampleMp3MediaFiles {
    private static final int NUM_TEST_ARTISTS = 10;
    private static final int NUM_TEST_ALBUMS_PER_ARTIST = 10;
    private static final int NUM_TEST_TRACKS_PER_ALBUM = 10;
    private static final int NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH = 10;
    private static final int START_DECADE = 1900;

    private CreateExampleMp3MediaFiles() {
        // Utility class
    }

    public static Path createExampleMediaInTarget() {
        try {
            final Path path = Paths.get("./target/generated-test-media").toRealPath();
            createExampleMedia(path);
            return path;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void createExampleMedia(final Path pathToCreateIn) {
        try {
            final byte[] emptyMp3File = readEmptyMp3File();

            for (int artistNum = 1; artistNum <= NUM_TEST_ARTISTS; artistNum++) {
                for (int albumNum = 1; albumNum <= NUM_TEST_ALBUMS_PER_ARTIST; albumNum++) {
                    for (int trackNum = 1; trackNum <= NUM_TEST_TRACKS_PER_ALBUM; trackNum++) {
                        createTestTrack(pathToCreateIn, emptyMp3File, artistNum, albumNum, trackNum);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static byte[] readEmptyMp3File() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in =
                new BufferedInputStream(CreateExampleMp3MediaFiles.class.getResourceAsStream("/empty.mp3"));

        try {
            byte[] bytes = new byte[4 * 1024];
            int len;
            while ((len = in.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            return out.toByteArray();
        } finally {
            in.close();
            out.close();
        }
    }

    private static void createTestTrack(final Path pathToCreateIn, final byte[] emptyMp3File, final int artistNum,
            final int albumNum, final int trackNum) throws IOException {
        final Path trackPath = makePathForThisTrack(pathToCreateIn, artistNum, albumNum, trackNum);
        final Path tmpEmptyTrackWithoutId3 = Paths.get(trackPath.toString() + ".tmp");
        if (!Files.exists(trackPath)) {
            makeParentPathIfItDoesntExist(trackPath);
            Files.write(tmpEmptyTrackWithoutId3, emptyMp3File);
            writeId3Tag(tmpEmptyTrackWithoutId3, trackPath, artistNum, albumNum, trackNum);
            Files.delete(tmpEmptyTrackWithoutId3);
        }
    }

    private static void makeParentPathIfItDoesntExist(Path trackPath) throws IOException {
        if (!Files.exists(trackPath.getParent())) {
            Files.createDirectories(trackPath.getParent());
        }
    }

    private static Path makePathForThisTrack(Path pathToCreateIn, int artistNum, int albumNum, int trackNum) {
        final String relativePath = String.format("./Test-Artist-%02d/Test-Album-%02d/Test-Track-%02d.mp3", artistNum,
                albumNum, trackNum);
        return pathToCreateIn.resolve(relativePath);
    }

    private static void writeId3Tag(final Path tmpEmptyTrackWithoutId3, final Path trackPath, final int artistNum,
            final int albumNum, final int trackNum) {
        try {
            final Mp3File mp3File = new Mp3File(tmpEmptyTrackWithoutId3);
            mp3File.removeCustomTag();
            mp3File.removeId3v1Tag();
            mp3File.removeId3v2Tag();

            final ID3v24Tag tag = new ID3v24Tag();
            tag.setArtist("Test Artist " + artistNum);
            tag.setAlbum("Test Album " + albumNum);
            tag.setTitle("Test Track " + trackNum);
            tag.setGenreDescription("Test Genre " + (artistNum % NUM_GENRES_TO_CYCLE_ARTISTS_THROUGH));
            tag.setYear(String.valueOf(START_DECADE + ((albumNum - 1) * 10)));

            mp3File.setId3v2Tag(tag);
            mp3File.save(trackPath.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
