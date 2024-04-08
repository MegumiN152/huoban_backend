package com.hh.yupao;

import java.util.Arrays;

/**
 * @author 黄昊
 * @version 1.0
 **/
public class suanfa2 {
    public static void main(String[] args) {
       String s="a good   example";
        System.out.println(reverseWords(s));
    }
    public static String reverseWords(String s) {
        s.trim();
        String[] str=s.split(" ");
        String ss="";
        for(int i=str.length-1;i>=0;i--){
            if (str[i].equals("")){
                continue;
            }
            str[i].trim();
            ss+=str[i]+" ";
        }
        return  ss.trim();
    }
}
