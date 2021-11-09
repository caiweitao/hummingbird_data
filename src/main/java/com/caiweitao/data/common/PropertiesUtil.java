package com.caiweitao.data.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author caiweitao
 * @Date 2021年5月17日
 * @Description 
 */
public class PropertiesUtil {

	/**
	 * 加载配置文件
	 * @param propertiesPath
	 * @return
	 */
	public static Properties getProperties (String propertiesPath) {
		//数据源配置
        Properties properties = new Properties();
        //通过当前类的class对象获取资源文件
        InputStream is;
		try {
			is = new FileInputStream(propertiesPath);
			properties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return properties;
	}
}
