package edu.ub.sd.tomcat.helloworld;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
    This allows not having to add in the web.xml the servlet mapping,
    but it requires Tomcat to support Servlet 3.0 spec.
 */
@WebServlet("/AnnotatedHelloWorld")
public class AnnotatedHelloWorld extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Counter</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>Annotated Hello World</p>");
        out.println("</html>");
    }

}
