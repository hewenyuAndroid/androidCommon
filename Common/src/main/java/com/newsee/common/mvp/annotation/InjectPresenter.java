package com.newsee.common.mvp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2018/9/17 17:30
 * 说明: View 层标注入 Presenter 实例的注解
 * ================================================
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectPresenter {

}
