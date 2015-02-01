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
package com.totalchange.discodj.xuggler;

import com.totalchange.discodj.catalogue.CatalogueException;
import com.xuggle.xuggler.IError;

final class XugglerException extends CatalogueException {
    private static final long serialVersionUID = 1L;

    XugglerException(String msg) {
        super(msg);
    }

    XugglerException(Throwable cause) {
        super(cause);
    }

    static void throwIfInError(int errorCode) throws XugglerException {
        if (errorCode >= 0) {
            return;
        }

        IError err = IError.make(errorCode);
        if (err != null) {
            throw new XugglerException(err.toString());
        } else {
            throw new XugglerException("Unidentified Xuggler exception with "
                    + "error code: " + errorCode);
        }
    }
}
