package com.totalchange.discodj.web.server;

import javax.inject.Inject;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandlerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

public class DiscoDjMediaHttpHandler extends StaticHttpHandlerBase {
    private static final String PARAM_ID = "id";

    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjMediaHttpHandler.class);

    private final Catalogue catalogue;

    @Inject
    public DiscoDjMediaHttpHandler(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    protected boolean handle(String uri, Request request, Response response)
            throws Exception {
        String mediaId = request.getParameter(PARAM_ID);
        Media media = catalogue.getMedia(mediaId);
        if (media != null && media.getFile() != null) {
            logger.trace("Serving up media {}", media);
            pickupContentType(response, media.getFile().getPath());
            sendFile(response, media.getFile());
            logger.trace("Served up media {}", media);
            return true;
        } else {
            logger.trace("Couldn't find media as referenced in request {}", uri);
            return false;
        }
    }
}
