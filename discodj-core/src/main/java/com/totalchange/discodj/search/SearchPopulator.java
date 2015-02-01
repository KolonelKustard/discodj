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
package com.totalchange.discodj.search;

import com.totalchange.discodj.media.Media;

public interface SearchPopulator {
    void addMedia(Media media) throws SearchException;
    void updateMedia(Media media) throws SearchException;
    void deleteMedia(String id) throws SearchException;
    void deleteAll() throws SearchException;
    void commit() throws SearchException;
}
