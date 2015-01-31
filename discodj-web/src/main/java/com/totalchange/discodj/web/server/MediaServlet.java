package com.totalchange.discodj.web.server;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.totalchange.discodj.catalogue.Catalogue;
import com.totalchange.servlicle.Servliclet;

@WebServlet("/media")
public final class MediaServlet extends Servliclet {
    private static final Logger logger = LoggerFactory
            .getLogger(MediaServlet.class);
    private static final long serialVersionUID = 1L;
    
    private Catalogue catalogue;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        logger.trace("Init-ing media servlet");
        Injector injector = (Injector) config.getServletContext().getAttribute(
                Injector.class.getName());
        catalogue = injector.getInstance(Catalogue.class);
        logger.trace("Init of media servlet finished");
    }

    @Override
    protected File gimmeFile(HttpServletRequest request,
            HttpServletResponse response) {
        return catalogue.getMedia(request.getParameter("id")).getFile();
    }
}
