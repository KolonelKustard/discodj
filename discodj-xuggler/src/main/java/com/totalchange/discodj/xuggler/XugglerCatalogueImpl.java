package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IMetaData;

public final class XugglerCatalogueImpl implements Catalogue {
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

    private void addFile(File file, Listener listener) {
        try {
            IContainer container = IContainer.make();
            XugglerException.throwIfInError(container.open(
                    file.getAbsolutePath(), null, null));
            try {
                IMetaData metadata = container.getMetaData();
                logger.trace("Got Xuggler metadata: {}", metadata);
                listener.yetMoreMedia(new XugglerMediaImpl(file
                        .getCanonicalPath(), metadata));
            } finally {
                logger.trace("Closing Xuggler container");
                XugglerException.throwIfInError(container.close());
            }
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
}
