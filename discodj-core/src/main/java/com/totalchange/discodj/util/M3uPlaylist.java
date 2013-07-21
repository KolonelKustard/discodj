package com.totalchange.discodj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class M3uPlaylist {
    private static final String COMMENT = "#";

    private static final Logger logger = LoggerFactory
            .getLogger(M3uPlaylist.class);

    private List<String> playlist = new ArrayList<>();

    public void read(Reader reader) throws IOException {
        logger.trace("Reading M3U data from reader {}", reader);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            logger.trace("Got line {}", line);
            line = line.trim();
            if (line.length() > 0 && !line.startsWith(COMMENT)) {
                logger.trace("Adding {} to playlist", line);
                playlist.add(line);
            }
        }
    }

    public void read(File file) throws IOException {
        logger.trace("Reading M3U playlist from file {}", file);
        Reader reader = new FileReader(file);
        try {
            read(reader);
        } finally {
            logger.trace("Closing reader");
            reader.close();
        }
    }

    public List<String> getPlaylist() {
        return playlist;
    }

    public List<File> getPlaylist(File root) {
        if (playlist == null) {
            return null;
        }

        List<File> filePlaylist = new ArrayList<>(playlist.size());
        for (String path : playlist) {
            File file = new File(root, path);
            if (file.exists() && file.isFile()) {
                filePlaylist.add(file);
            }
        }

        return filePlaylist;
    }
}
