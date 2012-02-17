package org.iotope.node.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Monitor of the gateway.
 */
public class UIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UIServlet() {
        super();
    }
    
    protected URI getResource(URI base, String path) {
        String[] parts = path.split("/");
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(parts));
        if (path.endsWith("/")) {
            list.add("");
        }
        
        return getResource(base, list);
    }
    
    protected URI getResource(URI base, List<String> path) {
        System.out.println(">>> " + base.toString());
        if (path.size() == 0)
            return base;
        
        String part = path.remove(0);
        while ((part.length() == 0) && path.size() > 0) {
            part = path.remove(0);
        }
        
        if (part.length() == 0 && path.size() == 0) {
            return getResource(base.resolve("index.html"), path);
        } else {
            if (path.size() > 0)
                return getResource(base.resolve(part + "/"), path);
            else
                return getResource(base.resolve(part), path);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            URL base = getClass().getResource("/META-INF/app/");
            URL url = getResource(base.toURI(), path).toURL();
            
            
            InputStream in = url.openStream();
            if (in != null) {
                if (path.endsWith("png")) {
                    resp.setContentType("image/png");
                } else if (path.endsWith("js")) {
                    resp.setContentType("application/javascript");
                } else if (path.endsWith("css")) {
                    resp.setContentType("text/css");
                } else {
                    resp.setContentType("text/html");
                }
                
                OutputStream out = resp.getOutputStream();
                copy(in, out);
                out.flush();
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        
    }
    
    protected void copy(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[512000];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }
    
    
}
