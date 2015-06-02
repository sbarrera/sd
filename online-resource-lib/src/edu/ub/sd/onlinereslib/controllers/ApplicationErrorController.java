package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.webframework.ServletDispatcher;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethod;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethodType;
import edu.ub.sd.onlinereslib.webframework.annotations.UrlPathController;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

@UrlPathController(path = "/error")
public class ApplicationErrorController extends WebController {

    @HttpMethod(type = HttpMethodType.GET)
    public void handleError() throws Exception {
        HttpSession session = request.getSession();
        String tmpl;

        Object o = session.getAttribute(ServletDispatcher.WebControllerExceptionAttr);

        if ( o == null ) {
            redirect(HomeController.class);
            return;
        }

        ServletDispatcher.WebControllerError error = (ServletDispatcher.WebControllerError) session.getAttribute(ServletDispatcher.WebControllerErrorTypeAttr);

        if ( error != ServletDispatcher.WebControllerError.NotFound ) {
            Exception ex = o == null ? null : (Exception) o;
            String message = "Null";
            String stackTrace = "";
            if ( ex != null ) {
                message = ex.getMessage() == null ? message : ex.getMessage();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                stackTrace = sw.toString();
            }

            setModel("httpStatusCode", error.getStatusCode());
            setModel("message", message);
            setModel("stackTrace", stackTrace);
            tmpl = "library/error";
        } else {
            tmpl = "library/fileNotFound";
        }

        session.removeAttribute(ServletDispatcher.WebControllerExceptionAttr);
        session.removeAttribute(ServletDispatcher.WebControllerErrorTypeAttr);

        view(tmpl);
    }

}
