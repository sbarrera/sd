package edu.ub.sd.onlinereslib.controllers.api;

import edu.ub.sd.onlinereslib.services.GsonSerializer;
import edu.ub.sd.onlinereslib.services.ResourceStore;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.*;

import java.io.PrintWriter;

@UrlPathController(path = "/API/{kind}/item/{name}")
public class ResourceDescriptorController extends WebController {

    @RequireService
    public ResourceStore resourceStore;

    @RequireService
    public GsonSerializer gson;

    @HttpMethod(type = HttpMethodType.GET)
    public void getItem(@HttpRequestParameter(name = "kind", fromUrl = true) String kind, @HttpRequestParameter(name = "name", fromUrl = true) String name) throws Exception {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        out.print(gson.serialize(resourceStore.getResourceByTypeAndId(kind, name)));
    }

}
