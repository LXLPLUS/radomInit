package com.lxkplus.RandomInit.utils;

import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;

public class TemplateUtils {
    /**
     * 应该是个需要保持单例的工厂
     * 标准的单例模式
     */
    static Configuration cfg = null;
    private static final String TEMPLATE_PATH = "src/main/java/com/lxkplus/RandomInit/template";

    static public Configuration getGlobalTemplate() throws IOException {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
        }
        return cfg;
    }
}
