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

import java.util.Comparator;

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
