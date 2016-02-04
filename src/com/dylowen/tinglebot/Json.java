package com.dylowen.tinglebot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Jan-2016
 */
public class Json {

    private static final Json SINGLETON = new Json();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Json() {
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public static Json get() {
        return SINGLETON;
    }
}
