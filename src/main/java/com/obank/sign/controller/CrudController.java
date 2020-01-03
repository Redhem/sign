package com.obank.sign.controller;

import java.util.TreeMap;

import com.obank.sign.exception.BadRequestException;
import com.obank.sign.util.RSAKeyUtils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CrudController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrudController.class);
    public static final String HTTP_HEADER_CLIENT_ID = "Client-Id";
    public static final String HTTP_HEADER_REQUEST_SIGNATURE = "Client-Signature";
    @Value("${client.client-001.publicKey}")
    private String publicKey;

    @PostMapping("/index")
    public void request(
            @RequestHeader(HTTP_HEADER_CLIENT_ID) String clientId, //HTTP请求头部信息clientId
            @RequestHeader(HTTP_HEADER_REQUEST_SIGNATURE) String signature, //HTTP请求头部信息签名
            @RequestBody TreeMap<String, Object> map, HttpServletRequest request) {
        LOGGER.debug("clientId:{},signature:{},request参数:{}", HTTP_HEADER_CLIENT_ID, signature, map);

        if (RSAKeyUtils.verify(map.toString(), publicKey, signature)) {
            LOGGER.debug("通过");
        } else {
            throw BadRequestException.invalidSignature(map.toString(), signature);
        }
    }

}