package com.huang;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test{


    private static void test1() throws Exception{
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        freemarker.cache.StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("aaa", "hello, ${user}");
        cfg.setTemplateLoader(stringTemplateLoader);
//        cfg.setTemplateLoader(new StringTemplateLoader("hello：${user}"));
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("aaa");
//        Template template = cfg.getTemplate();

        Map root = new HashMap();
        root.put("user", "lunzi");

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println(writer.toString());
    }

    private static void test2() throws Exception{
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        freemarker.cache.StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("aaa", "<#list fruits as fruit>\n" +
                "<li>${fruit}\n" +
                "</#list>");
        cfg.setTemplateLoader(stringTemplateLoader);
//        cfg.setTemplateLoader(new StringTemplateLoader("hello：${user}"));
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("aaa");
//        Template template = cfg.getTemplate();

        Map root = new HashMap();
        List list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        root.put("fruits", list);

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println(writer.toString());
    }
      
    public static void main(String[] args) throws Exception {
        test2();
    }  
}  