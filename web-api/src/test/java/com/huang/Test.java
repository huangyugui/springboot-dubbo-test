package com.huang;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test{


    private static void test1() throws Exception{
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        freemarker.cache.StringTemplateLoader s1 = new StringTemplateLoader();
        s1.putTemplate("aaa", "hello, ${user}");
        cfg.setTemplateLoader(s1);


//        cfg.setTemplateLoader(new StringTemplateLoader("hello：${user}"));
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("aaa");
//        Template template = cfg.getTemplate();

        Map root = new HashMap();
        root.put("user", "lunzi");

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println(writer.toString());

        s1.putTemplate("bbb", "world, ${user}");
        Template template1 = cfg.getTemplate("bbb");
        template1.process(root, writer);
        System.out.println(writer.toString());

        Object obj = s1.findTemplateSource("ccc");
        System.out.println("---->" + obj + "<--------");
    }

    private static void test2() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        freemarker.cache.StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("aaa", "<#list fruits as fruit>\n" +
                "<li>${fruit\n" +
                "</#list>\n" +
                "<font>${ddd.key}</font>");
        stringTemplateLoader.putTemplate("bbb", "");
        cfg.setTemplateLoader(stringTemplateLoader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template template = cfg.getTemplate("bbb");

        Map root = new HashMap();
        List list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        root.put("fruits", list);
        Map map = new HashMap();
        map.put("key", 1);
//        root.put("ddd", map);

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println("-----------------------------------------");
        System.out.println(writer.toString());
        System.out.println("-----------------------------------------");
    }
      
    public static void main(String[] args) {
        try {
            test2();
        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println("-----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("-----------------------------------------");
            e.printStackTrace();
        } catch (TemplateException e) {
            System.out.println("TemplateException");
            System.out.println("-----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("-----------------------------------------");
            e.printStackTrace();
        }

    }
}  