package com.czp.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 9:33 2019/1/4
 * @ Description：文件工具类
 * @ Modified By：
 * @ Version    : 1.0$
 */
public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 复制一个文件到另一个文件
     *
     * @param
     * @throws IOException
     */
    public static void copyFile(File oldFile, File newFile) {
        try {
            FileUtils.copyFile(oldFile, newFile);
        } catch (IOException e) {
            log.error("IO异常", e);
        }
    }

    /**
     * 读文件
     *
     * @param path
     * @return
     */
    public static String read(String path) {
        File file = new File(path);
        StringBuilder res = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public static String read(File file, String encoding) {
        try {
            return FileUtils.readFileToString(file, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void write(String cont, File dist) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
            writer.write(cont);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(File file, String data, String encoding) {
        write(file, data, encoding, false);
    }

    public static void write(File file, String data, String encoding, boolean append) {
        try {
            FileUtils.writeStringToFile(file, data, encoding, append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String read = read(new File("D:\\abc\\aabbcc_list.jsp"), "UTF-8");
        System.out.println(read);
    }

}
