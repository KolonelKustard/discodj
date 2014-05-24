package com.totalchange.discodj.media;

public abstract class AbstractMedia implements Media {
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
