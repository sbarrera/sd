package edu.ub.sd.sevenhalf.protocol.exception;

public class UnexpectedGameCommandException extends Exception {

    private String cmdHeader;

    public UnexpectedGameCommandException(String cmdHeader) {
        super();

        this.cmdHeader = cmdHeader;
    }

    public String getCommandHeader() {
        return cmdHeader;
    }

}
