package com.ycmm;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyGenerator {

    public static void main(String[] args) {
        ArrayList<String> warning = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File("F:\\Demo\\project\\Qin\\src\\main\\resources\\gen\\mybatis-gen.xml");
        ConfigurationParser cp = new ConfigurationParser(warning);
        Configuration config;

        try {
            config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback= new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator;
            try {
                myBatisGenerator = new MyBatisGenerator(config, callback, warning);
                myBatisGenerator.generate(null);
                System.out.println(warning);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
    }
}






















