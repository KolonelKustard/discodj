package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IMetaData;

public final class XugglerCatalogueImpl implements Catalogue {
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

    private boolean shouldAddFile(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".mp3")) {
            return true;
        } else if (name.endsWith(".mp4")) {
            return true;
        } else {
            return false;
        }
    }

    private Media makeMedia(String filename) throws XugglerException {
        IContainer container = IContainer.make();
        XugglerException.throwIfInError(container.open(filename,
                IContainer.Type.READ, null));
        try {
            IMetaData metadata = container.getMetaData();
            logger.trace("Got Xuggler metadata: {}", metadata);
            return new XugglerMediaImpl(filename, metadata);
        } finally {
            logger.trace("Closing Xuggler container");
            XugglerException.throwIfInError(container.close());
        }
    }

    private void addFile(File file, Listener listener) {
        try {
            listener.yetMoreMedia(makeMedia(file.getCanonicalPath()));
        } catch (Throwable th) {
            listener.warn("Couldn't read metadata from file " + file
                    + " with error: " + th.getMessage(), th);
        }
    }

    private void recurseForMedia(File dir, Listener listener) {
        for (File child : dir.listFiles()) {
            if (child.isFile() && shouldAddFile(child)) {
                addFile(child, listener);
            } else if (child.isDirectory()) {
                recurseForMedia(child, listener);
            }
        }
    }

    @Override
    public void listAllSongs(Listener listener) {
        recurseForMedia(root, listener);
    }

    @Override
    public Media getMedia(String mediaId) {
        try {
            return makeMedia(mediaId);
        } catch (XugglerException xugEx) {
            throw new RuntimeException(xugEx);
        }
    }

    @Override
    public List<Media> getDefaultPlaylist() {
        File file = new File(root, DEFAULT_PLAYLIST);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        
        // TODO Auto-generated method stub
        return null;
    }
}
