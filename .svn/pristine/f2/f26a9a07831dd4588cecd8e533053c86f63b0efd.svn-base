package com.jc.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


 
public final class JDBCUtil {
     
    //连接数据库的参数
    private static String url = null;
    private static String user = null;
    private static String driver = null;
    private static String password = null;
     
    private JDBCUtil () {
 
    }
 
    private static JDBCUtil instance = null;
 
    public static JDBCUtil getInstance() {
        if (instance == null) {
            synchronized (JDBCUtil.class) {
                if (instance == null) {
                    instance = new JDBCUtil();
                }
 
            }
        }
 
        return instance;
    }
     
    //配置文件
    private static Properties prop = new Properties();
     
    //注册驱动
    static {
        try {
            //利用类加载器读取配置文件
            InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("config/jdbc.properties");
            prop.load(is);
            url = prop.getProperty("wkxf.jdbcUrl");
            user = prop.getProperty("wkxf.user");
            driver = prop.getProperty("wkxf.driverClass");
            password = prop.getProperty("wkxf.password");
             
            Class.forName(driver);
             
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    //该方法获得连接
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
     
    //释放资源
    public void free(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                  
                e.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                          
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                  
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
   
  
    
    
}