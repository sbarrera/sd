package edu.ub.sd.tomcat.helloworld;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRedirectGet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/postRedirectGet.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean doHardWay = false;

        if ( doHardWay ) {
            String redirectedUrl = getAbsolutePathUrl(request, "404.jsp");
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.addHeader("Location", redirectedUrl);

            PrintWriter out = response.getWriter();
            out.println("Content moved permanently.");
            out.println("Your credit card is mine.");
            out.println("Please move along, there's nothing to be seen here.");
            out.println(String.format("goto: %s", redirectedUrl));
        } else {
            response.sendRedirect("/404.jsp");
        }
    }

    public String getAbsolutePathUrl(HttpServletRequest request, String relativePath) throws MalformedURLException {
        URL requestUrl = new URL(request.getRequestURL().toString());
        int port = requestUrl.getPort();
        String formatUrl = port == 80 ? "%s://%s/%s" : String.format("%%s://%%s:%s/%%s", port);
        String url = String.format(formatUrl,
                requestUrl.getProtocol(),
                requestUrl.getHost(),
                relativePath);
        return url;
    }

}
