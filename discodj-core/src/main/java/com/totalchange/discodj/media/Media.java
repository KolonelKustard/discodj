package com.totalchange.discodj.media;

import java.util.Date;

public interface Media {
    String getId();
    Date getLastModified();
    String getArtist();
    String getAlbum();
    String getGenre();
    int getYear();
    String getRequestedBy();
    String getTitle();
}
