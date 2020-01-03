package com.obank.sign.exception;

/**
 * BadRequestException
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -4215012927666110316L;
    private String code = "4000";

    public BadRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BadRequestException invalidSignature(String body, String signature) {
        return new BadRequestException("4001", "验证签名错误，被签名内容：" + body + "，签名为：" + signature);
    }
}