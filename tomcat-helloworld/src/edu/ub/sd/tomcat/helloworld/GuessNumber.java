package edu.ub.sd.tomcat.helloworld;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GuessNumber extends HttpServlet {

    private static int MAX = 1000;

    private static String SESSION_GUESSNUMBER = "guessNumber";

    public static String REQUEST_ATTR_WIN = "didWin";
    public static String REQUEST_ATTR_WRONGGUESS = "wrongGuess";
    public static String REQUEST_ATTR_NUMBER = "number";

    @Override
    public void init(ServletConfig configuration) throws ServletException {
        super.init(configuration);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer requestNumber = getRequestNumber(request);
        Integer sessionNumber = getSessionNumber(request);

        request.setAttribute(REQUEST_ATTR_WIN, Boolean.FALSE);
        request.setAttribute(REQUEST_ATTR_WRONGGUESS, Boolean.FALSE);
        request.setAttribute(REQUEST_ATTR_NUMBER, sessionNumber);

        if ( requestNumber != null ) {
            if ( requestNumber.equals(sessionNumber) ) {
                request.setAttribute(REQUEST_ATTR_WIN, Boolean.TRUE);

                sessionNumber = regenerateSessionNumber(request.getSession());
                request.setAttribute(REQUEST_ATTR_NUMBER, sessionNumber);
            } else {
                request.setAttribute(REQUEST_ATTR_WRONGGUESS, Boolean.TRUE);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/guessNumber.jsp");
        dispatcher.forward(request, response);
    }

    private Integer getSessionNumber(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Object guessNumber = session.getAttribute(SESSION_GUESSNUMBER);

        if ( guessNumber == null ) {
            guessNumber = regenerateSessionNumber(session);
        }

        return (Integer) guessNumber;
    }

    private Integer regenerateSessionNumber(HttpSession session) {
        Random random = new Random();
        Integer guessNumber = random.nextInt(MAX);
        session.setAttribute(SESSION_GUESSNUMBER, guessNumber);
        return guessNumber;
    }

    private Integer getRequestNumber(HttpServletRequest request) {
        String n = request.getParameter("n");

        if ( n != null ) {
            try {
                return Integer.parseInt(n);
            } catch ( Exception ex ) {
            }
        }

        return null;
    }

}
