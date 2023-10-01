package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.commons.CacheBuffer;
import com.lxkplus.RandomInit.model.FreeMakerMap;
import com.lxkplus.RandomInit.model.VO.SelectOption;
import com.lxkplus.RandomInit.service.RandomService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FreeMakerServiceImpl {

    @Resource
    Configuration configuration;

    @Resource
    RandomService randomService;

    @Resource
    ApplicationContext applicationContext;

    @Value("${template_path}")
    String path;

    @Resource
    CacheBuffer cacheBuffer;

    public String fillTemplate(String template, String actionID) {
        Object templateCache = cacheBuffer.get(actionID, "template");

        List<SelectOption> ruler = new ArrayList<>();
        if (templateCache == null) {
             ruler = randomService.getRuler(actionID);
             cacheBuffer.put(actionID, template, ruler);
        }
        else {
            if (templateCache instanceof List<?> list) {
                for (Object o : list) {
                    if (o instanceof SelectOption selectOption) {
                        ruler.add(selectOption);
                    }
                }
            }
        }

        FreeMakerMap freeMakerMap = applicationContext.getBean(FreeMakerMap.class);
        freeMakerMap.setList(ruler);

        String uuid = UUID.randomUUID().toString();
        try {
            Files.write(Path.of(path, uuid + ".ftl"), template.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            Template template1 = configuration.getTemplate(Path.of(uuid + ".ftl").toString());
            Writer out = Files.newBufferedWriter(Path.of(path, uuid + ".txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            template1.process(freeMakerMap, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        try {
            return Files.readString(Path.of(path, uuid + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
}
