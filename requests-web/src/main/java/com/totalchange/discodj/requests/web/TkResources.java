package com.totalchange.discodj.requests.web;

import org.takes.HttpException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Href;
import org.takes.rq.RqHref;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithType;
import org.takes.tk.TkWrap;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class TkResources extends TkWrap {
    private static final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

    public TkResources(final String resourcesRoot) {
        super(new Take() {
            @Override
            public Response act(Request request) throws IOException {
                final Href href = new RqHref.Base(request).href();
                final String resolvedPath = resolvePath(href);
                final String resource = resourcesRoot + resolvedPath;
                final InputStream in = getClass().getResourceAsStream(resource);
                if (in == null) {
                    throw new HttpException(HttpURLConnection.HTTP_NOT_FOUND, href.path() + " not found");
                }

                final String contentType = mimetypesFileTypeMap.getContentType(resolvedPath);
                return new RsWithType(new RsWithBody(in), contentType);
            }
        });
    }

    private static String resolvePath(final Href href) {
        if (href.path().equals("/")) {
            return "/index.html";
        } else {
            return href.path();
        }
    }
}
