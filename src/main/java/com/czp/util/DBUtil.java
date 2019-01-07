package com.czp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 8:32 2019/1/5
 * @ Description：MySQL工具类
 * @ Modified By：
 * @ Version    : 1.0$
 */

public class DBUtil {

    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "";
    private static String username = "";
    private static String password = "";

    public static void setDriver(String driver) {
        DBUtil.driver = driver;
    }

    public static void setUrl(String url) {
        DBUtil.url = url;
    }

    public static void setUsername(String username) {
        DBUtil.username = username;
    }

    public static void setPassword(String password) {
        DBUtil.password = password;
    }

    // 数据库操作
    private static final String SQL = "SELECT * FROM ";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    private static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            log.error("getTableNames failure", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                closeConnection(conn);
            } catch (SQLException e) {
                log.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @ return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            log.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     *
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            log.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }

    /**
     * 获取表中字段的所有注释
     *
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
//        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt;
        String tableSql = SQL + tableName;
        //列名注释集合
        List<String> columnComments = new ArrayList<>();
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }


    /**
     * 获取表中字段的所有注释,没有注释时用列名替代
     *
     * @param tableName
     */
    public static List<String> getFormatColumnComments(String tableName) {
        List<String> turnList = new ArrayList<>();
        List<String> columnNameList = getColumnNames(tableName);
        List<String> columnCommentList = getColumnComments(tableName);
        for (int i = 0; i < columnCommentList.size(); i++) {
            String str = columnCommentList.get(i);
            if ("".equals(str)) {
                turnList.add(columnNameList.get(i));
            } else {
                turnList.add(str);
            }
        }
        return turnList;
    }

}
