package com.caiweitao.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caiweitao
 * @Date 2021年5月6日
 * @Description 注释实体类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    String tableName() default "";//数据库表名，没有注明时，默认实体类名首字母小写作为表名

}
