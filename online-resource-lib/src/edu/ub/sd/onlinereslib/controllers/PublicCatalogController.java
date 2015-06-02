package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.beans.User;
import edu.ub.sd.onlinereslib.services.ResourceStore;
import edu.ub.sd.onlinereslib.services.UserStore;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethod;
import edu.ub.sd.onlinereslib.webframework.annotations.HttpMethodType;
import edu.ub.sd.onlinereslib.webframework.annotations.RequireService;
import edu.ub.sd.onlinereslib.webframework.annotations.UrlPathController;

@UrlPathController(path = "/cataleg")
public class PublicCatalogController extends WebController {

    @RequireService
    public ResourceStore resourceStore;

    @RequireService
    public UserStore userStore;

    @HttpMethod(type = HttpMethodType.GET)
    public void index() throws Exception {
        log("Catalog controller running...");

        User user = userStore.getUser(request);

        setModel("user", user);
        if ( user != null ) {
            setModel("tokens", userStore.getTokens(user));
            setModel("userAcquisitions", resourceStore.getUserBoughtResources(request));
            setModel("userShoppingBasket", resourceStore.getUserShoppingBasket(request));
            setModel("tokensAvailable", true);
        }

        setModel("books", resourceStore.getBookResources());
        setModel("music", resourceStore.getMusicResources());
        setModel("videos", resourceStore.getVideosResources());

        view("library/cataleg");
    }

}
