package com.lxkplus.RandomInit.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class JsonUtils {
    public Faker faker = new Faker(Locale.CHINA);
    public ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    JsonUtils() {
        yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
