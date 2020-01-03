package com.obank.sign;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.obank.sign.util.RSAKeyUtils;
import com.obank.sign.util.SignUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DemoApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationTests.class);
    @Autowired
    private TestRestTemplate testRestTemplate;
    ParameterizedTypeReference<Map<String, Object>> clz = new ParameterizedTypeReference<Map<String, Object>>() {
    };;
    public static final String HTTP_HEADER_CLIENT_ID = "Client-Id";
    public static final String HTTP_HEADER_REQUEST_SIGNATURE = "Client-Signature";
    @Value("${privateKey}")
    private String privateKey;

    @Test
    void successTest() throws RestClientException, URISyntaxException {
        LOGGER.debug("按照字段顺序排序");
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("a-key", "1");
        treeMap.put("c-key", "2");
        treeMap.put("b-key", "3");

        String treeMapStr = treeMap.toString();
        LOGGER.debug("签名内容:{}", treeMapStr);
        String signStr = SignUtils.sign(treeMapStr, privateKey);
        // testRestTemplate.postForEntity(new URI("/index"), map, Object.class);
        ResponseEntity<Map<String, Object>> result = testRestTemplate
                .exchange(RequestEntity.post(new URI("/index"))
                        .header(HTTP_HEADER_CLIENT_ID, "client-001")
                        .header(HTTP_HEADER_REQUEST_SIGNATURE, signStr)
                        .body(treeMap), clz);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void errorTest() throws RestClientException, URISyntaxException {
        LOGGER.debug("不按照字段顺序排序");
        Map<String, Object> map = new HashMap<>();
        map.put("a-key", "1");
        map.put("c-key", "2");
        map.put("b-key", "3");

        String mapStr = map.toString();
        LOGGER.debug("签名内容:{}", mapStr);
        //String signStr = SignUtils.sign(mapStr, privateKey);
        String signStr = RSAKeyUtils.sign(map.toString(), privateKey);
        // testRestTemplate.postForEntity(new URI("/index"), map, Object.class);
        ResponseEntity<Map<String, Object>> result = testRestTemplate
                .exchange(RequestEntity.post(new URI("/index"))
                        .header(HTTP_HEADER_CLIENT_ID, "client-001")
                        .header(HTTP_HEADER_REQUEST_SIGNATURE, signStr)
                        .body(map), clz);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        LOGGER.debug("result:{}", result.getBody());
    }

}
