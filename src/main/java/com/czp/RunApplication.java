package com.czp;

import com.czp.util.DBUtil;
import com.czp.util.FileUtil;
import com.czp.util.RegexUtil;
import com.czp.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * @ author     ：CZP.
 * @ Date       ：Created in 16:55 2019/1/3
 * @ Description：生成html文件
 * @ Modified By：
 * @ Version    : 1.0$
 */
public class RunApplication {

    private static final Logger log = LoggerFactory.getLogger(RunApplication.class);
    /**
     * 本程序可以根据实体类属性一键生成适用于yimi后台系统的代码
     */

    //需要生成文件的表名
    private static String TABLE_NAME = "";

    //jsp文件的title
    private static String TITLE = "";

    //生成文件的路径
    private static String DEST_PATH = "";

    //生成web文件的路径
    private static String WEB_PATH = "";


    private static final String ACTION = "Action";

    private static final String SERVICE = "Service";

    private static final String IMPL = "Impl";

    private static final String ADD = "_add";

    private static final String DETAIL = "_detail";

    private static final String LIST = "_list";

    private static final String JSP = ".jsp";

    private static final String PREFIX = "I";

    private static final String JAVA = ".java";

    private static Map<String, String> packageName = new HashMap<>(4);

    public static void setPackageName(Map<String, String> packageName) {
        RunApplication.packageName = packageName;
    }

    //正则匹配符，全匹配(包括换行符)
    private static final String REGX = "<!-- begin -->([\\s\\S]*)<!-- end -->";

    //java模板文件   "src/main/resources/templates/";
    private static String TEMPLATES = RunApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "templates/";


    //jsp模板文件  "src/main/resources/webTemplates/";
    private static String WEB_TEMPLATES = RunApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "webTemplates/";


    //全局变量，用来存储模板下后台文件的集合，key --> 文件名，value --> 文件的绝对路径
    private static Map<String, String> fileMap = new HashMap<>();

    //全局变量，用来存储模板下web文件的集合，key --> 文件名，value --> 文件的绝对路径
    private static Map<String, String> webFileMap = new HashMap<>();


    //加载资源文件
    private static String setTemplates(String ext) {
        String jar = RunApplication.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        JarFile jf = null;
        try {
            jf = new JarFile(jar);
            Enumeration<JarEntry> es = jf.entries();
            while (es.hasMoreElements()) {
                String resname = es.nextElement().getName();
                if (resname.lastIndexOf(ext) != -1) {
                    URL url = RunApplication.class.getResource("/".concat(resname));
                    return url.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jf != null) {
                try {
                    jf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void run(String tableName, String title, String destPath, String webPath) {
        String path = RunApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains(".jar")) {
            TEMPLATES = "ttr/templates/";
            WEB_TEMPLATES = "ttr/webTemplates/";
        }
        TABLE_NAME = tableName;
        TITLE = title;
        DEST_PATH = destPath;
        WEB_PATH = webPath;

        File file = new File(DEST_PATH);
        File webFile = new File(WEB_PATH);

        try {
            FileUtils.copyDirectory(new File(TEMPLATES), file);
        } catch (IOException e) {
            log.error("复制后台文件失败！", e);
        }
        log.info("创建后台文件目录成功！");
        try {
            FileUtils.copyDirectory(new File(WEB_TEMPLATES), webFile);
        } catch (IOException e) {
            log.error("复制WEB文件失败！", e);
        }
        log.info("创建WEB文件目录成功！");

        fileMap = traverseFolder(file.getAbsolutePath(), fileMap);
        boolean b2 = renameFileInfo(fileMap);
        if (b2) {
            log.info("修改文件名成功！");
        } else {
            log.info("修改文件失败！");
        }
        webFileMap = traverseFolder(webFile.getAbsolutePath() + "\\aabbcc", webFileMap);
        boolean b3 = renameFileInfo(webFileMap);
        if (b3) {
            log.info("修改WEB文件名成功！");
        } else {
            log.info("修改WEB文件名失败！");
        }
        String aabbcc = StringUtil.upperTable(false, TABLE_NAME);
        String aaBbCc = StringUtil.firstLower(StringUtil.upperTable(true, TABLE_NAME));
        boolean b = renameDirectory(aabbcc, WEB_PATH + "/" + "aabbcc");
        if (b) {
            log.info("修改WEB文件夹名成功！");
        } else {
            log.error("修改WEB文件夹名失败！");
        }
        boolean b1 = renameTitleAndAbc(title, aabbcc, aaBbCc);
        if (b1) {
            log.info("修改WEB文件标题成功！");
        } else {
            log.info("修改WEB文件标题成功！");
        }
        editFileInfo(file, fileMap);
        log.info("修改后台文件内容成功！");
        editFileInfo(webFile, webFileMap);
        log.info("修改WEB文件内容成功！");

        log.info("文件生成成功，可以到{}目录下查看。", file.getAbsolutePath());

    }

    /**
     * 修改文件内容
     *
     * @param file
     * @param fileMap
     */
    private static void editFileInfo(File file, Map<String, String> fileMap) {
        if (fileMap.isEmpty()) {
            Map<String, String> newFileMap = traverseFolder(file.getAbsolutePath(), fileMap);
            if (!(Objects.requireNonNull(newFileMap)).isEmpty()) {
                for (Map.Entry<String, String> vo : fileMap.entrySet()) {
                    if (vo.getKey().endsWith(JAVA)) {
                        editFileContent(vo.getKey(), vo.getValue());
                    } else if (vo.getKey().endsWith(JSP)) {
                        createTableInfo(vo.getValue(), TABLE_NAME);
                    }
                }

            }
            fileMap.clear();
        } else {
            throw new RuntimeException("fileMap不为空,出现异常！");
        }
    }

    /**
     * 修改文件名
     *
     * @param map
     */
    private static boolean renameFileInfo(Map<String, String> map) {
        boolean b = false;
        if (!(Objects.requireNonNull(map)).isEmpty()) {
            for (Map.Entry<String, String> vo : map.entrySet()) {
                String key = vo.getKey();
                String value = vo.getValue();
                if (renameFile(key, value)) {
                    b = true;
                }
            }
            map.clear();
        }
        return b;
    }

    /**
     * 获取大小写
     *
     * @param tableName
     * @return
     */
    private static Map<String, String> getAnyCase(String tableName) {
        Map<String, String> map = new HashMap<>(3);
        String aabbcc = StringUtil.upperTable(false, tableName);
        String AaBbCc = StringUtil.upperTable(true, tableName);
        String aaBbCc = StringUtil.firstLower(StringUtil.upperTable(true, tableName));
        map.put("aabbcc", aabbcc);
        map.put("AaBbCc", AaBbCc);
        map.put("aaBbCc", aaBbCc);
        return map;
    }

    /**
     * 获取文件夹的所有文件
     *
     * @param path
     * @return
     */
    private static Map<String, String> traverseFolder(String path, Map<String, String> map) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if ((null == files) || (files.length == 0)) {
                log.error("文件为空！");
                return null;
            }
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    traverseFolder(file2.getAbsolutePath(), map);
                } else {
                    map.put(file2.getName(), file2.getAbsolutePath());
                }
            }
            return map;
        }
        log.error("文件不存在！");
        return null;
    }

    /**
     * 重命名文件夹
     */
    private static boolean renameDirectory(String newName, String fileName) {
        if (newName == null || fileName == null) {
            log.error("非法参数！");
            return false;
        }
        File file = new File(fileName);
        if (file.exists() && file.isDirectory()) {
            String path = file.getAbsolutePath();
            if (file.getAbsolutePath().contains("\\")) {
                if (path.endsWith("\\")) {
                    path = path.substring(0, path.length() - 1);
                } else {

                    String filePath = path.substring(0, path.lastIndexOf("\\") + 1);
                    File newFile = new File(filePath + newName);
                    if (checkFileExist(newFile)) {
                        return file.renameTo(newFile);
                    }
                }


            }
            return false;

        }
        log.error("文件夹不存在,修改文件夹名称失败！");
        return false;
    }

    /**
     * 重命名之前检查文件是否存在
     *
     * @param file
     * @return
     */
    private static boolean checkFileExist(File file) {
        if (file.exists()) {
            if (file.delete()) {
                log.info("删除源文件成功");
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    /**
     * 重命名文件名
     *
     * @param newName
     * @param fileName
     * @return
     */
    private static boolean renameFile(String newName, String fileName) {
        Map<String, String> anyCase = getAnyCase(TABLE_NAME);
        File file = new File(fileName);
        fileName = fileName.substring(0, fileName.lastIndexOf("\\"));
        StringBuilder webFilePath = new StringBuilder(fileName).append("\\");
        if (newName.endsWith(ACTION + JAVA)) {
            File newFile = new File(fileName + "\\" + anyCase.get("AaBbCc") + ACTION + JAVA);
            if (checkFileExist(newFile)) {
                return file.renameTo(newFile);
            }
            return false;
        }
        if (newName.endsWith(SERVICE + JAVA)) {
            File newFile = new File(fileName + "\\" + PREFIX + anyCase.get("AaBbCc") + SERVICE + JAVA);
            if (checkFileExist(newFile)) {
                return file.renameTo(newFile);
            }
            return false;
        }
        if (newName.endsWith(SERVICE + IMPL + JAVA)) {
            File newFile = new File(fileName + "\\" + anyCase.get("AaBbCc") + SERVICE + IMPL + JAVA);
            if (checkFileExist(newFile)) {
                return file.renameTo(newFile);
            }
            return false;
        }
        String aabbcc = anyCase.get("aabbcc");
        Boolean b1 = getBoolean(newName, file, webFilePath, ADD, aabbcc);
        if (b1 != null) {
            return b1;
        }
        Boolean b2 = getBoolean(newName, file, webFilePath, DETAIL, aabbcc);
        if (b2 != null) {
            return b2;
        }
        Boolean b3 = getBoolean(newName, file, webFilePath, LIST, aabbcc);
        if (b3 != null) {
            return b3;
        }
        return false;
    }

    /**
     * 判断重命名是否成功
     *
     * @param newName
     * @param file
     * @param webFilePath
     * @param add
     * @return
     */
    private static Boolean getBoolean(String newName, File file, StringBuilder webFilePath, String add, String
            aabbcc) {
        if (newName.endsWith(add + JSP)) {
            String path = webFilePath.append(aabbcc).append(add).append(JSP).toString();
            File newFile = new File(path);
            if (checkFileExist(newFile)) {
                return file.renameTo(newFile);
            }
        }
        return null;
    }

    /**
     * 修改后台文件内容
     *
     * @param fileName
     * @param filePath
     */
    private static void editFileContent(String fileName, String filePath) {
        StringBuilder sb = new StringBuilder();

        if (fileName.endsWith(ACTION + JAVA)) {
            sb.append("package ").append(packageName.get("action")).append(";\n\n");
            sb.append("import ").append(packageName.get("entity")).append(".").append(packageName.get("domain")).append(";\n");
            sb.append("import ").append(packageName.get("service")).append(".").append(PREFIX).append(packageName.get("domain")).append(SERVICE).append(";\n");
            sb.append("import ").append("com.lw.common.page.Pager").append(";\n");
            sb.append("import ").append("com.lw.core.base.action.BaseAction").append(";\n");
        } else if (fileName.endsWith(SERVICE + JAVA)) {
            sb.append("package ").append(packageName.get("service")).append(";");
            sb.append("import ").append(packageName.get("entity")).append(".").append(packageName.get("domain")).append(";\n");
            sb.append("import ").append("com.lw.core.base.service.BaseService").append(";\n");
        } else if (fileName.endsWith(IMPL + JAVA)) {
            sb.append("package ").append(packageName.get("service")).append(".impl").append(";");
            sb.append("import ").append(packageName.get("entity")).append(".").append(packageName.get("domain")).append(";\n");
            sb.append("import ").append(packageName.get("service")).append(".").append(PREFIX).append(packageName.get("domain")).append(SERVICE).append(";\n");
            sb.append("import ").append("com.lw.core.base.service.impl.BaseServiceImpl").append(";\n");
        }
        Map<String, String> anyCase = getAnyCase(TABLE_NAME);
        File src = new File(filePath);
        String cont = FileUtil.read(src, "UTF-8");
        cont = cont.replaceAll("aabbcc", anyCase.get("aabbcc"))
                .replaceAll("AaBbCc", anyCase.get("AaBbCc"))
                .replaceAll("aaBbCc", anyCase.get("aaBbCc"));
        sb.append(cont);
        FileUtil.write(src, sb.toString(), "UTF-8");
    }

    /**
     * 生成JSP页面table信息
     *
     * @param filePath
     * @param tableName
     * @return
     */
    private static void createTableInfo(String filePath, String tableName) {
        File src = new File(filePath);
        if (!src.exists() || src.isDirectory()) {
            log.error("文件不存在或者不是文件");
        } else {
            //读取文件内容
            String read = FileUtil.read(src, "UTF-8");
            String oldTableInfo = RegexUtil.getSubUtilSimple(read, REGX);
            if (filePath.endsWith(ADD + JSP)) {
                //获取模板table之间的内容
                String newTableInfo = createAddTableInfo(tableName);
                String tableInfo = read.replace(oldTableInfo, newTableInfo);
                FileUtil.write(src, tableInfo, "UTF-8");
            } else if (filePath.endsWith(DETAIL + JSP)) {
                String newTableInfo = createEditTableInfo(tableName);
                String tableInfo = read.replace(oldTableInfo, newTableInfo);
                FileUtil.write(src, tableInfo, "UTF-8");
            } else if (filePath.endsWith((LIST + JSP))) {
                String newTableInfo = createListTableInfo(tableName);
                String tableInfo = read.replace(oldTableInfo, newTableInfo);
                FileUtil.write(src, tableInfo, "UTF-8");
            } else {
                log.error("生成的文件有误，请重新生成！");
                throw new RuntimeException("生成的文件有误，请重新生成！");
            }
        }
    }

    /**
     * 添加页面的table
     */
    private static String createAddTableInfo(String tableName) {
        //获取字段名
        List<String> columnNames = DBUtil.getColumnNames(tableName);

        //获取注释
        List<String> formatColumnComments = DBUtil.getFormatColumnComments(tableName);

        //获取字段类型
        List<String> columnTypes = DBUtil.getColumnTypes(tableName);

        removePrimary(tableName, columnNames, formatColumnComments, columnTypes);
        List<String> jspTypeList = columnTypes2Jsp(columnTypes);
        String type = "text";
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < columnNames.size(); i++) {
            String name = StringUtil.firstLower(StringUtil.upperTable(true, columnNames.get(i)));
            StringBuilder tr = new StringBuilder();
            tr.append("\n<tr>\n")
                    .append("<td class=\"info col-md-1 text-right\"><span class=\"red\">*</span>").append(formatColumnComments.get(i)).append("</td>\n")
                    .append("<td class=\"col-md-11\">\n");
            if ("date".equals(jspTypeList.get(i))) {
                tr.append("<input type=\"text\" style=\"text-align: left;\" class=\"form-control\" required\n")
                        .append("name=").append("\"").append(name).append("\" ")
                        .append("placeholder=\"").append("\"请选择").append(formatColumnComments.get(i)).append("\" \n")
                        .append("onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" onchange=\"\" />\n");
            } else {
                if ("number".equals(jspTypeList.get(i))) {
                    type = "number";
                }
                tr.append("<input type=\"").append(type).append("\" class=\"form-control\" value=\"\" maxlength=\"50\" datatype=\"*1-50\"\n")
                        .append("name=").append("\"").append(name).append("\" ")
                        .append("placeholder=").append("\"请输入").append(formatColumnComments.get(i)).append("\" ")
                        .append("nullmsg=").append("\"请输入").append(formatColumnComments.get(i)).append("\" ")
                        .append("errormsg=").append("\"").append(formatColumnComments.get(i)).append("至少1个字符,最多50个字符！\" />\n");
            }
            tr.append("</td>\n").append("</tr>\n");

            table.append(tr);
        }
        return table.toString();
    }

    /**
     * 修改页面的table
     */
    private static String createEditTableInfo(String tableName) {
        //获取字段名
        List<String> columnNames = DBUtil.getColumnNames(tableName);

        //获取注释
        List<String> formatColumnComments = DBUtil.getFormatColumnComments(tableName);

        //获取字段类型
        List<String> columnTypes = DBUtil.getColumnTypes(tableName);
        List<String> jspTypeList = columnTypes2Jsp(columnTypes);

        removePrimary(tableName, columnNames, formatColumnComments, columnTypes);

        String aaBbCc = StringUtil.firstLower(StringUtil.upperTable(true, tableName));
        String type = "text";
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < columnNames.size(); i++) {
            String name = StringUtil.firstLower(StringUtil.upperTable(true, columnNames.get(i)));
            StringBuilder tr = new StringBuilder();
            tr.append("\n<tr>\n")
                    .append("<td class=\"info col-md-1 text-right\"><span class=\"red\">*</span>").append(formatColumnComments.get(i)).append("</td>\n")
                    .append("<td class=\"col-md-11\">\n");
            if ("date".equals(jspTypeList.get(i))) {
                tr.append("<input type=\"text\" style=\"text-align: left;\" class=\"form-control\" required\n")
                        .append("name=").append("\"").append(name).append("\" ")
                        .append("placeholder=\"").append("\"请选择").append(formatColumnComments.get(i)).append("\" \n")
                        .append("onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" \n")
                        .append("value=\"<fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='").append("${").append(aaBbCc).append(".").append(name).append("}' />\">");
            } else {
                if ("number".equals(jspTypeList.get(i))) {
                    type = "number";
                }
                tr.append("<input type=\"").append(type).append("\" class=\"form-control\" value=\"\" maxlength=\"50\" datatype=\"*1-50\"\n")
                        .append("name=").append("\"").append(name).append("\" ")
                        .append("value=").append("\"${").append(aaBbCc).append(".").append(name).append("}\" ")
                        .append("placeholder=").append("\"请输入").append(formatColumnComments.get(i)).append("\" ")
                        .append("nullmsg=").append("\"请输入").append(formatColumnComments.get(i)).append("\" ")
                        .append("errormsg=").append("\"").append(formatColumnComments.get(i)).append("至少1个字符,最多50个字符！\" />\n");
            }
            tr.append("</td>\n").append("</tr>\n");
            table.append(tr);
        }
        return table.toString();
    }

    /**
     * 移除主键
     * @param tableName
     * @param columnNames
     * @param formatColumnComments
     * @param columnTypes
     */
    private static void removePrimary(String tableName, List<String> columnNames, List<String> formatColumnComments, List<String> columnTypes) {
        String[] primaryKeys = DBUtil.getPrimaryKeys(tableName);

        if (primaryKeys != null && primaryKeys.length > 0) {
            for (String primaryKey : primaryKeys) {
                int id = columnNames.indexOf(primaryKey);
                formatColumnComments.remove(id);
                columnTypes.remove(id);
                columnNames.remove(primaryKey);
            }
        }
    }

    /**
     * 列表页面的table
     */
    private static String createListTableInfo(String tableName) {
        //获取小写表名
        String aabbcc = StringUtil.upperTable(false, tableName);
        //获取字段名
        List<String> columnNames = DBUtil.getColumnNames(tableName);
        //获取注释
        List<String> formatColumnComments = DBUtil.getFormatColumnComments(tableName);
        //获取字段类型
        List<String> columnTypes = DBUtil.getColumnTypes(tableName);
        List<String> jspTypeList = columnTypes2Jsp(columnTypes);

        StringBuilder table = new StringBuilder();
        StringBuilder thead = new StringBuilder();
        StringBuilder tbody = new StringBuilder();
        String type = "text";
        tbody.append("\n<tbody>\n")
                .append("<c:forEach items=\"${pager.datas}\" var=\"item\">\n")
                .append("<tr>\n");
        thead.append("\n<thead>\n")
                .append("<tr>\n");
        for (int i = 0; i < columnNames.size(); i++) {

            String name = StringUtil.firstLower(StringUtil.upperTable(true, columnNames.get(i)));

            thead.append("<th>").append(formatColumnComments.get(i)).append("</th>\n");
            if ("date".equals(jspTypeList.get(i))) {
                tbody.append("<td><fmt:formatDate pattern=\"yyyy-MM-dd HH:mm:ss\" value=\"${item.").append(name).append("}\"> /></td>\n");
            } else {
                tbody.append("<td>${item.").append(name).append("}</td>\n");
            }

        }
        thead.append("<th>操作</th>\n")
                .append("</tr>\n")
                .append("</thead>\n");
        tbody.append("<td>\n")
                .append("<a href=\"<%=basePath%>manage/").append(aabbcc).append("/${item.id}.html\">编辑</a>\n")
                .append("<a href=\"javascript:void(0)\" onclick=\"LW.").append(aabbcc).append(".del(${item.id})\">删除</a></td>\n")
                .append("</tr>\n")
                .append("</c:forEach>\n")
                .append("</tbody>\n");
        table.append(thead);
        table.append(tbody);
        return table.toString();
    }


    /**
     * 对jsp页面的标题重命名
     *
     * @return
     */
    private static boolean renameTitleAndAbc(String title, String aabbcc, String aaBbCc) {
        File webFile = new File(WEB_PATH);
        Map<String, String> map = new LinkedHashMap<>();
        map = traverseFolder(webFile.getAbsolutePath(), map);
        if (!(Objects.requireNonNull(map)).isEmpty()) {
            for (Map.Entry<String, String> vo : map.entrySet()) {
                String read = FileUtil.read(new File(vo.getValue()), "UTF-8");
                read = read.replace("TITLE", title).replace("aabbcc", aabbcc).replace("aaBbCc", aaBbCc);

                FileUtil.write(new File(vo.getValue()), read, "UTF-8");
            }
            return true;
        }
        return false;
    }

    private static List<String> columnTypes2Jsp(List<String> columnTypes) {
        List<String> jspTypes = new LinkedList<>();
        for (String columnType : columnTypes) {
            if (columnType.contains("INT")) {
                jspTypes.add("number");
            } else if (columnType.contains("DATE") || columnType.contains("TIME")) {
                jspTypes.add("date");
            } else {
                jspTypes.add("text");
            }
        }
        return jspTypes;
    }

    /*public static void main(String[] args) {
        DBUtil.setUrl("jdbc:mysql://127.0.0.1:3306/traceability?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
        DBUtil.setUsername("root");
        DBUtil.setPassword("742003942");

        List<String> ad_pic = DBUtil.getColumnTypes("member_door_authority_log");
        System.out.println(ad_pic.toString());
        System.out.println("OK!!!");

        List<String> list = columnTypes2Jsp(ad_pic);
        System.out.println(list.toString());
    }*/
}

