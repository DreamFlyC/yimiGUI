package com.czp.util;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 9:32 2019/1/4
 * @ Description：字符串工具类
 * @ Modified By：
 * @ Version    : 1.0$
 */
public class StringUtil {

    private static final String UNDERLINE = "_";

    private static final char CHAR_a = 'a';

    private static final char CHAR_z = 'z';

    private static final char CHAR_A = 'A';

    private static final char CHAR_Z = 'Z';


    /**
     * @param firstUpper 是否转换大小写， true : 转换为大写  false：不转换
     * @param str
     * @return
     * @example false, "as_d_w" --> asdw  true, "as_d_w" --> AsDW
     */
    public static String upperTable(boolean firstUpper, String str) {
        StringBuilder sbf = new StringBuilder();
        if (str.contains(UNDERLINE)) {
            String[] split = str.split(UNDERLINE);
            for (String s : split) {
                String upperTable = upperTable(firstUpper, s);
                sbf.append(upperTable);
            }
        } else {
            char[] ch = str.toCharArray();
            if ((firstUpper) &&
                    (ch[0] >= CHAR_a) && (ch[0] <= CHAR_z)) {
                ch[0] = ((char) (ch[0] - ' '));
            }
            sbf.append(ch);
        }
        return sbf.toString();
    }


    /**
     * 首字母转换小写
     *
     * @return
     */
    public static String firstLower(String str) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            char[] ch = str.toCharArray();
            if (ch[0] >= CHAR_A && ch[0] <= CHAR_Z) {
                ch[0] = (char) (ch[0] + 32);
            }
            return sb.append(ch).toString();
        }
        return null;
    }
}
