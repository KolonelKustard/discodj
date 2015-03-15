/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.util.M3uPlaylist;

public final class FileTikaCatalogueImpl implements Catalogue {
    private static final String[] SUPPORTED_FILE_EXTENSIONS = { ".mp3", ".mp4" };
    private static final String DEFAULT_PLAYLIST = "default.m3u";

    private static final Logger logger = LoggerFactory
            .getLogger(FileTikaCatalogueImpl.class);

    private File root;

    public FileTikaCatalogueImpl(File root) throws FileNotFoundException {
        logger.trace("Creating new Xuggler catalogue for root {}", root);

        if (root == null) {
            throw new NullPointerException("Root directory for catalogue "
                    + "cannot be null");
        }

        if (!root.exists()) {
            throw new FileNotFoundException("Root directory "
                    + root.getAbsolutePath() + " does not exist");
        }

        if (!root.isDirectory()) {
            throw new FileNotFoundException("Root " + root.getAbsolutePath()
                    + " is not a directory");
        }

        this.root = root;
    }

    @Inject
    public FileTikaCatalogueImpl(@Named("catalogueRoot") String rootFilename)
            throws FileNotFoundException {
        this(new File(rootFilename));
    }

    private Media makeMedia(String filename) throws FileTikaException {
        File file = new File(filename);

        if (!isBeneathParent(root, file)) {
            throw new FileTikaException(file + " is not a child of " + root);
        }

        Tika tika = new Tika();
        Metadata metadata = new Metadata();
        try {
            logger.trace("Reading metadata for {}", filename);
            InputStream in = TikaInputStream.get(file, metadata);
            Reader reader = tika.parse(in, metadata);
            logger.trace("Metadata read to {}", metadata);
            in.close();
            reader.close();
            return new FileTikaMediaImpl(file, metadata);
        } catch (Exception ex) {
            throw new FileTikaException(ex);
        }
    }

    private boolean isBeneathParent(File parent, File file) {
        File dir = file;
        while ((dir = dir.getParentFile()) != null) {
            if (dir.equals(parent)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById() {
        return new FileCatalogueEntityIterator(root, SUPPORTED_FILE_EXTENSIONS);
    }

    @Override
    public Media getMedia(String mediaId) throws FileTikaException {
        return makeMedia(mediaId);
    }

    @Override
    public List<Media> getDefaultPlaylist() {
        File file = new File(root, DEFAULT_PLAYLIST);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        M3uPlaylist m3u = new M3uPlaylist();
        try {
            m3u.read(file);

            List<File> files = m3u.getPlaylist(root);
            List<Media> playlist = new ArrayList<>(files.size());
            for (File media : files) {
                try {
                    playlist.add(makeMedia(media.getCanonicalPath()));
                } catch (FileTikaException xEx) {
                    logger.info(
                            "Skipped adding item " + file
                                    + " to default playlist with error: "
                                    + xEx.getMessage(), xEx);
                }
            }
            return playlist;
        } catch (IOException ioEx) {
            logger.error(
                    "Error reading default playlist: " + ioEx.getMessage(),
                    ioEx);
            return Collections.emptyList();
        }
    }
}
