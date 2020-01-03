package com.obank.sign.util;

public enum ExceptionEnum {
    SIGN_ERROR("4001", "签名内容不合法");

    private String code;
    private String message;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}