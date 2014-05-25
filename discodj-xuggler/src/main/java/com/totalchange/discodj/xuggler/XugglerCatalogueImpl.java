package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;
import com.totalchange.discodj.util.M3uPlaylist;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IMetaData;

public final class XugglerCatalogueImpl implements Catalogue {
    private static final String[] SUPPORTED_FILE_EXTENSIONS = { ".mp3", ".mp4" };
    private static final String DEFAULT_PLAYLIST = "default.m3u";

    private static final Logger logger = LoggerFactory
            .getLogger(XugglerCatalogueImpl.class);

    private File root;

    public XugglerCatalogueImpl(File root) throws FileNotFoundException {
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
    public XugglerCatalogueImpl(@Named("catalogueRoot") String rootFilename)
            throws FileNotFoundException {
        this(new File(rootFilename));
    }

    private Media makeMedia(String filename) throws XugglerException {
        File file = new File(filename);

        IContainer container = IContainer.make();
        XugglerException.throwIfInError(container.open(filename,
                IContainer.Type.READ, null));
        try {
            IMetaData metadata = container.getMetaData();
            logger.trace("Got Xuggler metadata: {}", metadata);
            return new XugglerMediaImpl(filename, file.lastModified(), metadata);
        } finally {
            logger.trace("Closing Xuggler container");
            XugglerException.throwIfInError(container.close());
        }
    }

    @Override
    public Iterator<CatalogueEntity> listAllAlphabeticallyById() {
        return new FileCatalogueEntityIterator(root, SUPPORTED_FILE_EXTENSIONS);
    }

    @Override
    public Media getMedia(String mediaId) throws XugglerException {
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
                } catch (XugglerException xEx) {
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

    @Override
    public InputStream getMediaData(Media media) throws IOException {
        return new FileInputStream(media.getId());
    }
}
