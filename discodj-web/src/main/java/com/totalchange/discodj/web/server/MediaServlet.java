package com.totalchange.discodj.web.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

@Singleton
@WebServlet("/media")
public final class MediaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(MediaServlet.class);

    public static final String PATH = "/media";
    public static final String PARAM_ID = "id";

    private Catalogue catalogue;

    @Inject
    public MediaServlet(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(PARAM_ID);
        logger.trace("Received request for ID {}", id);

        if (id != null && id.length() > 0) {
            Media media;
            InputStream in;
            try {
                media = catalogue.getMedia(id);
                in = catalogue.getMediaData(media);
            } catch (IOException ioEx) {
                logger.error("Error fetching media", ioEx);
                throw new ServletException("Failed to get media item with id "
                        + id, ioEx);
            }

            // TODO: Work with ranges
            response.addHeader("Accept-Ranges", "none");

            response.setContentLength((int) new File(id).length());

            // TODO Improve content type handling
            if (media.getId().toLowerCase().endsWith(".mp3")) {
                logger.trace("MP3 content type for media {}", media);
                response.setContentType("audio/mpeg");
            } else if (media.getId().toLowerCase().endsWith(".mp4")) {
                logger.trace("MP4 content type for media {}", media);
                response.setContentType("video/mp4");
            } else {
                logger.warn("Unknown content type for media {}", media);
            }

            try {
                logger.trace("Streaming back");
                IOUtils.copy(in, response.getOutputStream());
                logger.trace("Finished streaming");
            } finally {
                in.close();
            }
        }
    }
}
