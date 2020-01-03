package com.obank.sign.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ScfException.class)
    public ResponseEntity<Object> handleScfRequestException(ScfException e) {
        logger.debug("handle client exception:{}", e);
        Map<String, String> map = new HashMap<>();
        map.put("code", "4001");
        map.put("message", "签名错误");
        logger.debug("返回客户端信息:{}", map);
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        logger.debug("handle client exception:{}", e);
        Map<String, String> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("message", e.getMessage());
        logger.debug("返回客户端信息:{}", map);
        return ResponseEntity.badRequest().body(map);
    }
}
