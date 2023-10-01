package com.lxkplus.RandomInit.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.javafaker.Faker;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

@Component
public class BeanInit {

    @Value("${template_path}")
    String path;

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

    @Bean
    Configuration getFreeMakerBean() {
        Configuration configuration  = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setDefaultEncoding("UTF-8");
        try {
            Files.createDirectories(Path.of(path));
            configuration.setDirectoryForTemplateLoading(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
