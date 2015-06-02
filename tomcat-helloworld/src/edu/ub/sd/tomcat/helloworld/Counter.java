package edu.ub.sd.tomcat.helloworld;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Counter extends HttpServlet {

    private Integer count = null;

    @Override
    public void init(ServletConfig configuration) throws ServletException {
        super.init(configuration);
        count = 0;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int currentCount;

        synchronized (this) {
            currentCount = ++count;
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Counter</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(String.format("<h1>Count amount: %s</h1>", currentCount));
        out.println("</body>");
        out.println("</html>");
    }

}
