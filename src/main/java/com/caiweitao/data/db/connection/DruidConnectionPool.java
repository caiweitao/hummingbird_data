package com.caiweitao.data.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.caiweitao.data.common.PropertiesUtil;

/**
 * @author 蔡伟涛
 * @Date 2021年5月6日
 * @Description Druid连接池
 */
public class DruidConnectionPool {
	//维护多数据源
	private final static Map<String,DruidDataSource> dataSourceMap = new HashMap<String,DruidDataSource>();
	
	/**
	 * 初始化数据源，一般在项目启动时调用
	 * @param root 项目根路径
	 * @param druidProperties 要注册的数据源properties配置文件名（比如  game_db.properties）
	 */
	public static void init (String root, String...druidProperties) {
		if (druidProperties == null) {
			return;
		}
		for (String properties:druidProperties) {
			//默认用配置文件名作为数据源名
			int beginIndex = 0;
			if (properties.contains("/")) {
				beginIndex = properties.lastIndexOf("/") + 1;
			}
			String dataSoruceName = properties.substring(beginIndex, properties.indexOf("."));
			createDataSource(dataSoruceName, root + properties);
		}
	}

	/**
	 * 创建数据源
	 * @param dataSoruceName
	 * @param propertiesPath
	 * @return
	 */
	private static DataSource createDataSource (String dataSoruceName, String propertiesPath) {
		if (dataSourceMap.containsKey(dataSoruceName)) {
			return dataSourceMap.get(dataSoruceName);
		}
		DruidDataSource dataSource = null;
		Properties properties = PropertiesUtil.getProperties(propertiesPath);
        try {
			dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (dataSource != null) {
        	dataSourceMap.put(dataSoruceName, dataSource);
        	System.out.println(String.format("注册数据源【%s】【%s】", dataSoruceName,propertiesPath));
        }
        return dataSource;
	}
	
	/**
	 * 获得一个数据库连接
	 * @param dataSoruceName 数据源名字（代表哪个数据库）
	 * @return
	 */
	public static Connection getDbConnection (String dataSoruceName) {
		DataSource ds = dataSourceMap.get(dataSoruceName);
		Objects.requireNonNull(ds);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭数据源
	 */
	public static void shutdown () {
		for (Entry<String,DruidDataSource> en:dataSourceMap.entrySet()) {
			DruidDataSource ds = en.getValue();
			ds.close();
		}
	}
	
	/**
	 * 关闭连接
	 * @param con
	 * @throws SQLException
	 */
    public static void close(Connection con) throws SQLException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new SQLException("关闭连接时异常", e);
        }finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new SQLException("关闭连接时异常", e);
            }
        }
    }
}
