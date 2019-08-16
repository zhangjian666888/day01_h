package com.zj.exception;

public class LoginException extends Exception {

    private static final long serialVersionUID = 3568605135730664888L;

    public LoginException(){
        super();
    }

    public LoginException(String message){
        super(message);
    }

}
