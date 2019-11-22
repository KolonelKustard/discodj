package com.totalchange.discodj.media.file;

import com.mpatric.mp3agic.*;
import com.totalchange.discodj.server.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

class FileMedia implements Media {
    private static final Logger logger = LoggerFactory.getLogger(FileMedia.class);

    private final String sourceId;
    private final String id;
    private final long lastModifiedMs;
    private final String artist;
    private final String album;
    private final String genre;
    private final int year;
    private final String title;
    private final URI uri;

    FileMedia(final String sourceId, final Path mediaPath) throws IOException, InvalidDataException,
            UnsupportedTagException {
        final Path realMediaPath = mediaPath.toRealPath();

        this.sourceId = sourceId;
        this.id = realMediaPath.toString();
        this.lastModifiedMs = Files.getLastModifiedTime(mediaPath).toMillis();
        this.uri = realMediaPath.toUri();

        final Mp3File mp3File = new Mp3File(mediaPath);
        if (mp3File.hasId3v2Tag()) {
            final ID3v2 tag = mp3File.getId3v2Tag();
            this.artist = tag.getArtist();
            this.album = tag.getAlbum();
            this.genre = tag.getGenreDescription();
            this.year = parseYear(tag.getYear());
            this.title = tag.getTitle();
        } else if (mp3File.hasId3v1Tag()) {
            final ID3v1 tag = mp3File.getId3v1Tag();
            this.artist = tag.getArtist();
            this.album = tag.getAlbum();
            this.genre = tag.getGenreDescription();
            this.year = parseYear(tag.getYear());
            this.title = tag.getTitle();
        } else {
            throw new FileMediaException("Media file " + id + " doesn't appear to have valid ID3 tags");
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public long getLastModifiedMs() {
        return lastModifiedMs;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getRequestedBy() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    private int parseYear(final String year) {
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            logger.debug("Failed to parse year from {} for ID3 tag in {}", year, id, ex);
            return 0;
        }
    }
}
