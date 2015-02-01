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
