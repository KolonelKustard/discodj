package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.media.Media;

/**
 * <p>
 * Represents a catalogue/library of songs.
 * </p>
 * 
 * @author Ralph Jones
 */
public interface Catalogue {
    public interface Listener {
        void yetMoreMedia(Media media);
        void warn(String msg, Throwable cause);
    }
    
    /**
     * <p>
     * Lists all the songs in the catalogue and blocks until completed. Numerous
     * threads may be used by the implemention and events are passed to the
     * {@link Catalogue.Listener} passed in as an argument.
     * </p>
     * 
     * @param listener
     *            receives events as the catalogue is listed
     */
    void listAllSongs(Listener listener);
}