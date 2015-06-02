package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.beans.Resource;
import edu.ub.sd.onlinereslib.beans.ResourceList;
import edu.ub.sd.onlinereslib.beans.User;
import edu.ub.sd.onlinereslib.services.ResourceStore;
import edu.ub.sd.onlinereslib.services.UserStore;
import edu.ub.sd.onlinereslib.services.exceptions.AlreadyBoughtResourceException;
import edu.ub.sd.onlinereslib.services.exceptions.AlreadyInShoppingBasketException;
import edu.ub.sd.onlinereslib.services.exceptions.NotEnoughTokensException;
import edu.ub.sd.onlinereslib.services.exceptions.ResourceNotFoundException;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.*;

@UrlPathController(path = "/protegit/llista")
public class UserCatalogController extends WebController {

    static String MessageTypeAttr = "_MESSAGE_TYPE_ATTR_";
    static String MessageContentAttr = "_MESSAGE_CONTENT_ATTR_";

    @RequireService
    public ResourceStore resourceStore;

    @RequireService
    public UserStore userStore;

    @HttpMethod(type = HttpMethodType.GET)
    public void index() throws Exception {
        log("User based catalog running");

        User user = userStore.getUser(request);

        setModel("user", user);
        setModel("resources", resourceStore.getUserShoppingBasket(request));
        setModel("boughtResources", resourceStore.getUserBoughtResources(request));
        setModel("tokens", userStore.getTokens(user));

        updateModelMessage();

        view("library/userCataleg");
    }

    @HttpMethod(type = HttpMethodType.POST, action = "addResourceToShoppingBasket")
    public void addResourceToShoppingBasket(@HttpRequestParameter(name = "resource") String id) throws Exception {
        log(String.format("Adding to shopping basket resource [%s]", id));
        try {
            Resource resource = resourceStore.addResourceToShoppingBasket(request, id);
            setMessage("info", String.format("El recurs <b>%s</b> s'ha afegit a la cistella de compra.", resource.getName()));
        } catch ( Exception e ) {
            treatBusinessException(e);
        }

        redirect(UserCatalogController.class);
    }

    @HttpMethod(type = HttpMethodType.POST, action = "removeResourceFromShoppingBasket")
    public void removeResourceFromShoppingBasket(@HttpRequestParameter(name = "resource") String id) throws Exception {
        try {
            Resource resource = resourceStore.removeResourceFromShoppingBasket(request, id);
            setMessage("info", String.format("El recurs <b>%s</b> s'ha tret de la cistella de compra.", resource.getName()));
        } catch ( ResourceNotFoundException e ) {
            treatBusinessException(e);
        }

        redirect(UserCatalogController.class);
    }

    @HttpMethod(type = HttpMethodType.POST, action = "buyResources")
    public void buyResources() throws Exception {
        try {
            ResourceList shoppingBasket = resourceStore.buyResourcesFromShoppingList(request);
            setMessage("success", String.format("Els recursos <b>%s</b> s'han comprat correctament. S'ha descontat <b>%s&cent;</b>", getListResources(shoppingBasket), shoppingBasket.getTotalCost()));
        } catch (NotEnoughTokensException e) {
            treatBusinessException(e);
        }

        redirect(UserCatalogController.class);
    }

    @HttpMethod(type = HttpMethodType.POST, action = "oneClickBuy")
    public void performOneClickBuy(@HttpRequestParameter(name = "resource") String id) throws Exception {
        try {
            Resource resource = resourceStore.buyResource(request, id);
            setMessage("success", String.format("El recurs <b>%s</b> s'ha comprat correctament. Se li ha descontat <b>%s&cent;</b>", resource.getName(), resource.getCost()));
        } catch (Exception e) {
            treatBusinessException(e);
        }

        redirect(UserCatalogController.class);
    }

    private void treatBusinessException(Exception ex) throws Exception {
        if ( ex instanceof ResourceNotFoundException ) {
            setMessage("warning", "El recurs demanat no existeix.");
        } else if ( ex instanceof AlreadyInShoppingBasketException ) {
            setMessage("warning", "El recurs que es vol afegir ja hi &eacute;s a la cistella de compra.");
        } else if ( ex instanceof AlreadyBoughtResourceException ) {
            setMessage("warning", "El recurs que es vol afegir ja est&agrave; comprat.");
        } else if ( ex instanceof NotEnoughTokensException ) {
            setMessage("warning", "Saldo insuficient per fer la compra. Afegeixi m&eacute;s diners, garrepa.");
        } else {
            throw ex;
        }
    }

    private void setMessage(String type, String message) {
        setSessionAttribute(MessageTypeAttr, type);
        setSessionAttribute(MessageContentAttr, message);
    }

    private void updateModelMessage() {
        setModel("messageType", getSessionAttribute(MessageTypeAttr));
        setModel("messageContent", getSessionAttribute(MessageContentAttr));
        setMessage(null, null);
    }

    private String getListResources(ResourceList list) {
        StringBuilder stringBuilder = new StringBuilder("");

        for ( int i = 0 ; i < list.size() ; i++ ) {
            Resource resource = list.get(i);
            stringBuilder.append(resource.getName());

            if ( i != list.size() - 1 ) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

}
