package edu.ub.sd.onlinereslib.beans;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements Serializable {

    private String tomcatUsername;
    private String name;
    private boolean isNew;

    public String getTomcatUsername() {
        return tomcatUsername;
    }

    public void setTomcatUsername(String tomcatUsername) {
        this.tomcatUsername = tomcatUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return String.format("[User name [%s]]", name);
    }

}
