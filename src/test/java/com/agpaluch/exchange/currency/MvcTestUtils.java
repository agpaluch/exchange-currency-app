package com.agpaluch.exchange.currency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class MvcTestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static String getFromBody(MvcResult mvcResult, String jsonPath) {
        try {
            String json = mvcResult.getResponse().getContentAsString();
            return JsonPath.read(json, jsonPath);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    public static String asJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}
