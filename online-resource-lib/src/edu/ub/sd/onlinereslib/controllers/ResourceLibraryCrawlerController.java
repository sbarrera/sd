package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethod;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethodType;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpRequestParameter;
import edu.ub.sd.onlinereslib.webframework.annotations.UrlPathController;

import javax.servlet.ServletContext;

@UrlPathController(path = "/consulta")
public class ResourceLibraryCrawlerController extends WebController {

    @HttpMethod(type = HttpMethodType.GET)
    public void index() throws Exception {
        view("library/crawler");
    }

}
