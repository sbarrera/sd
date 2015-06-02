package edu.ub.sd.onlinereslib.services.exceptions;

public class AlreadyInShoppingBasketException extends Exception {

    public AlreadyInShoppingBasketException(String id) {
        super(String.format("Resource [%s] already in shopping basket", id));
    }

}
