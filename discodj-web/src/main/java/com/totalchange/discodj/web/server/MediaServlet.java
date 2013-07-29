package com.totalchange.discodj.web.server;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.discodj.media.Media;

@Singleton
public final class MediaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        if (id != null && id.length() > 0) {
            Media media;
            InputStream in;
            try {
                media = catalogue.getMedia(id);
                in = catalogue.getMediaData(media);
            } catch (IOException ioEx) {
                throw new ServletException("Failed to get media item with id "
                        + id, ioEx);
            }

            // TODO Pass back content type

            try {
                IOUtils.copy(in, response.getOutputStream());
            } finally {
                in.close();
            }
        }
    }
}
