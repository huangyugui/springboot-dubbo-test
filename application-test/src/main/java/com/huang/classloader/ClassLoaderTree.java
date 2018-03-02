package com.huang.classloader;

public class ClassLoaderTree {
    public static void main(String[] args) {
        String str = "";
        ClassLoader loader = ClassLoaderTree.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }
        System.out.println(loader);
    }
}