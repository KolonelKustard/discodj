package com.totalchange.discodj.catalogue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.totalchange.discodj.media.Media;

/**
 * <p>
 * Represents a catalogue/library of songs.
 * </p>
 * 
 * @author Ralph Jones
 */
public interface Catalogue {
    public interface CatalogueEntity {
        String getId();
        Date getLastModified();
    }

    Iterator<CatalogueEntity> listAllAlphabeticallyById();
    Media getMedia(String mediaId);
    List<Media> getDefaultPlaylist();
    InputStream getMediaData(Media media) throws IOException;
}
