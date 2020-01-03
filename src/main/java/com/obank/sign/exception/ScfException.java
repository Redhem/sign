package com.obank.sign.exception;

import com.obank.sign.util.ExceptionEnum;

/**
 * ScfException
 */
public class ScfException extends RuntimeException {

    private static final long serialVersionUID = -281630154023053444L;
    private String code;
    private String message;

    public ScfException(String message) {
        super(message);
    }

    public ScfException(ExceptionEnum e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}