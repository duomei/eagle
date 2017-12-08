package cn.jf.annotation;

import java.lang.annotation.*;

/**
 * 用户操作日志注解类
 * 用于通过AOP方式收集用户操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {

    String value() default "";

}
