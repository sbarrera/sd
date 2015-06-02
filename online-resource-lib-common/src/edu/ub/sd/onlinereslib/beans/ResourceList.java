package edu.ub.sd.onlinereslib.beans;

import java.util.concurrent.CopyOnWriteArrayList;

public class ResourceList extends CopyOnWriteArrayList<Resource> {

    public int getTotalCost () {
        int sum = 0;

        for ( Resource resource : this ) {
            sum += resource.getCost();
        }

        return sum;
    }

    public String[] getResourceIds() {
        String[] ids = new String[size()];
        for ( int i = 0 ; i < size() ; i++ ) {
            ids[i] = get(i).getId();
        }
        return ids;
    }

}
