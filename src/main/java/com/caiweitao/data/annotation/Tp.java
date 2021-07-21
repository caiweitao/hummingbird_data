package com.caiweitao.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 蔡伟涛
 * @Date 2021年5月7日
 * @Description 标注long类型的时间戳属性，被该标注的属性，入库时会被转成java.sql.Timestamp对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tp {

}
