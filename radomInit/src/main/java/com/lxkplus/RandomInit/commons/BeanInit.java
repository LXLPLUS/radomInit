package com.lxkplus.RandomInit.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BeanInit {

    @Bean
    Faker faker() {
        return new Faker(Locale.CHINA);
    }

    @Bean
    YamlObjectMapper yamlMapper() {
        return new YamlObjectMapper();
    }

    public static class YamlObjectMapper {

        ObjectMapper objectMapper;

        YamlObjectMapper() {
            objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }

        public ObjectMapper yaml() {
            return objectMapper;
        }

        public String writeValueAsString(Object value) throws JsonProcessingException {
            return StringUtils.removeStart(objectMapper.writeValueAsString(value), "---\n");
        }
    }
}
