package com.huang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) {
        filter("1");
        filter("2");
        filter("3");
        filter("4");
        filter("5");

    }

    private static void filter(String t){
        String str = "D186783E36B721651E8AF96AB1C4000B";
        long nBegin = System.currentTimeMillis();
        for (int i = 0; i < 1024 * 1024; i++) {
            if(t.equals("1")){
                str = filter1(str);
            }else if(t.equals("2")){
                str = filter2(str);
            }else if(t.equals("3")){
                str = filter3(str);
            }else if(t.equals("4")){
                str = filter4(str);
            }else if(t.equals("5")){
                str = filter5(str);
            }
        }
        long nEnd = System.currentTimeMillis();

        System.out.println(nEnd - nBegin);
    }

    private static String filter1(String strOld) {
        String strNew = new String();
        for (int i = 0; i < strOld.length(); i++) {
            if ('0' <= strOld.charAt(i) && strOld.charAt(i) <= '9') {
                strNew += strOld.charAt(i);
            }
        }
        return strNew;
    }

    private static String filter2(String strOld) {
        StringBuffer strNew = new StringBuffer();
        for (int i = 0; i < strOld.length(); i++) {
            if ('0' <= strOld.charAt(i) && strOld.charAt(i) <= '9') {
                strNew.append(strOld.charAt(i));
            }
        }
        return strNew.toString();
    }

    private static String filter3(String strOld) {
        StringBuffer strNew = new StringBuffer();
        int nLen = strOld.length();
        for (int i = 0; i < nLen; i++) {
            char ch = strOld.charAt(i);
            if ('0' <= ch && ch <= '9') {
                strNew.append(ch);
            }
        }
        return strNew.toString();
    }

    private static String filter4(String strOld) {
        int nLen = strOld.length();
        StringBuffer strNew = new StringBuffer(nLen);
        for (int i = 0; i < nLen; i++) {
            char ch = strOld.charAt(i);
            if ('0' <= ch && ch <= '9') {
                strNew.append(ch);
            }
        }
        return strNew.toString();
    }

    private static String filter5(String strOld) {
        int nLen = strOld.length();
        char[] chArray = new char[nLen];
        int nPos = 0;
        for (int i = 0; i < nLen; i++) {
            char ch = strOld.charAt(i);
            if ('0' <= ch && ch <= '9') {
                chArray[nPos] = ch;
                nPos++;
            }
        }
        return new String(chArray, 0, nPos);
    }
}
