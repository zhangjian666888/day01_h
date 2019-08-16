package com.zj.exception;

public class CastException extends ClassCastException {

    private static final long serialVersionUID = -3080015308263709109L;

    public CastException() {
        super();
    }

    public CastException(String s) {
        super(s);
    }
}
