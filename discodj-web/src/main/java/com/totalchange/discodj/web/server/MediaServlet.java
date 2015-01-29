package com.totalchange.discodj.web.server;

import java.io.File;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.totalchange.servlicle.Servliclet;

@WebServlet("/media")
public final class MediaServlet extends Servliclet {
    private static final long serialVersionUID = 1L;

    @Override
    protected File gimmeFile(HttpServletRequest request,
            HttpServletResponse response) {
        return new File(request.getParameter("id"));
    }
}
