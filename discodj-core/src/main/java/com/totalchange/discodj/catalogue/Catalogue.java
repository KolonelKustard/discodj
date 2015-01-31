package com.totalchange.discodj.catalogue;

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

    Iterator<CatalogueEntity> listAllAlphabeticallyById()
            throws CatalogueException;
    Media getMedia(String mediaId) throws CatalogueException;
    List<Media> getDefaultPlaylist() throws CatalogueException;
}
