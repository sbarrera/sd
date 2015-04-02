package edu.ub.sd.sevenhalf.protocol.exception;

public class MalformedGameCommandException extends Exception {

    public MalformedGameCommandException(String reason) {
        super(reason);
    }

}
