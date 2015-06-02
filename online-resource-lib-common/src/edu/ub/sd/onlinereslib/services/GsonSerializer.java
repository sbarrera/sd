package edu.ub.sd.onlinereslib.services;

import com.google.gson.Gson;
import edu.ub.sd.onlinereslib.beans.Resource;
import edu.ub.sd.onlinereslib.webframework.ServiceController;
import edu.ub.sd.onlinereslib.webframework.annotations.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GsonSerializer extends ServiceController {

    private Gson gson;

    @Override
    public void initialize() {
        gson = new Gson();
    }

    @Override
    public void shutdown() {
        log("Gson service shutting down");
    }

    public String serialize(Object o) {
        return gson.toJson(o);
    }

    public String serialize(Collection<Resource> list) {
        List<ResourceListItem> proxy = new ArrayList<>();
        for ( Resource resource : list ) {
            proxy.add(new ResourceListItem(resource));
        }
        return gson.toJson(proxy);
    }

    public String serialize(Resource resource) {
        return gson.toJson(new ResourceSimple(resource));
    }

    public String[] getStringListFromFile(File file) throws Exception {
        log(String.format("Loading json [%s]", file.getAbsolutePath()));
        return gson.fromJson(new BufferedReader(new FileReader(file)), String[].class);
    }

    private class ResourceListItem {

        public String NAME;
        public String DESC;

        public ResourceListItem(Resource resource) {
            NAME = resource.getName();
            DESC = resource.getName();
        }

    }

    private class ResourceSimple {

        public String PRICE;
        public String LINK;

        public ResourceSimple(Resource resource) {
            PRICE = "" + resource.getCost();
            LINK = String.format("/llibreria/protegit/descarrega/%s", resource.getId());
        }

    }

}
