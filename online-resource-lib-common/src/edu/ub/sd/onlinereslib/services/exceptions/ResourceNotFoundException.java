package edu.ub.sd.onlinereslib.services.exceptions;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String id) {
        super(String.format("Resource [%s] not available", id));
    }

}
