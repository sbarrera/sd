package edu.ub.sd.onlinereslib.services.exceptions;

public class AlreadyBoughtResourceException extends Exception {

    public AlreadyBoughtResourceException(String id) {
        super(String.format("Resource [%s] already bought", id));
    }
}
