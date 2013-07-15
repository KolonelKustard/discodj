package com.totalchange.discodj.catalogue;

import java.util.Comparator;

import com.totalchange.discodj.media.Media;

final class MediaComparator implements Comparator<Media> {
    private int compare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        } else if (s1 != null && s2 == null) {
            return 1;
        } else if (s1 == null && s2 != null) {
            return -1;
        } else {
            return s1.compareTo(s2);
        }
    }

    private int compare(int i1, int i2) {
        return i1 > i2 ? +1 : i1 < i2 ? -1 : 0;
    }

    @Override
    public int compare(Media m1, Media m2) {
        if (m1 == null && m2 == null) {
            return 0;
        } else if (m1 != null && m2 == null) {
            return 1;
        } else if (m1 == null && m2 != null) {
            return -1;
        } else {
            int c = compare(m1.getArtist(), m2.getArtist());
            if (c != 0) {
                return c;
            }

            c = compare(m1.getAlbum(), m2.getAlbum());
            if (c != 0) {
                return c;
            }

            c = compare(m1.getYear(), m2.getYear());
            if (c != 0) {
                return c;
            }

            c = compare(m1.getGenre(), m2.getGenre());
            if (c != 0) {
                return c;
            }

            c = compare(m1.getTitle(), m2.getTitle());
            if (c != 0) {
                return c;
            }

            return 0;
        }
    }
}
