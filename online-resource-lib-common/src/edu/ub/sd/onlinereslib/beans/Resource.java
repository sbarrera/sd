package edu.ub.sd.onlinereslib.beans;

import java.io.File;
import java.io.Serializable;

public class Resource implements Serializable {

    private String id;
    private String name;
    private String mimeType;
    private String extension;
    private int cost;
    private File file;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return String.format("[Resource id [%s] name [%s] mime-type [%s]]", id, name, mimeType);
    }

}
