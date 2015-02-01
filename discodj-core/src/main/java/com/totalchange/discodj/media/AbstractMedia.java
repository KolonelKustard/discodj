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
package com.totalchange.discodj.media;

public abstract class AbstractMedia implements Media, Comparable<Media> {
    @Override
    public int compareTo(Media media) {
        return new MediaComparator().compare(this, media);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Media)) {
            return false;
        }

        Media other = (Media) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "id=" + getId() + ", lastModified=" + getLastModified()
                + ", artist=" + getArtist() + ", album=" + getAlbum()
                + ", genre=" + getGenre() + ", year=" + getYear()
                + ", requestedBy=" + getRequestedBy() + ", title=" + getTitle();
    }
}
