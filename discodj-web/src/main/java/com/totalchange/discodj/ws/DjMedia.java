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
package com.totalchange.discodj.ws;

import java.io.Serializable;

public class DjMedia implements Serializable {
    private static final long serialVersionUID = 8853328652712059275L;

    private String id;
    private String artist;
    private String title;
    private int whenCanBePlayedAgain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWhenCanBePlayedAgain() {
        return whenCanBePlayedAgain;
    }

    public void setWhenCanBePlayedAgain(int whenCanBePlayedAgain) {
        this.whenCanBePlayedAgain = whenCanBePlayedAgain;
    }
}
