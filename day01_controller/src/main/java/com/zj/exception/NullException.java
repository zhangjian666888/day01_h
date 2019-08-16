package com.zj.exception;

public class NullException extends NullPointerException {

    private static final long serialVersionUID = -1986522532531296027L;

    public NullException() {
        super();
    }

    public NullException(String message) {
        super(message);
    }

}
