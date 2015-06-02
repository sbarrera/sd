package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.services.UserStore;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethod;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethodType;
import edu.ub.sd.onlinereslib.webframework.annotations.RequireService;
import edu.ub.sd.onlinereslib.webframework.annotations.UrlPathController;

@UrlPathController(path = "/logout")
public class LogoutController extends WebController {

    @RequireService
    public UserStore userStore;

    @HttpMethod(type = HttpMethodType.GET)
    public void index() throws Exception {
        userStore.logout(request);
        redirect(HomeController.class);
    }

}
