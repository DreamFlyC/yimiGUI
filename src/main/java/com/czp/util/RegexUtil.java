package com.czp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 15:06 2019/1/5
 * @ Description：正则工具类
 * @ Modified By：
 * @ Version    : 1.0$
 */
public class RegexUtil {
    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param soap
     * @return
     */
    public static List<String> getSubUtil(String soap,String rgex){
        List<String> list = new ArrayList<>();
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);

        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
     * @param soap
     * @param rgex
     * @return
     */
    public static String getSubUtilSimple(String soap,String rgex){
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        if(m.find()){
            return m.group(1);
        }
        return "";
    }
}
