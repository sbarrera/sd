package edu.ub.sd.onlinereslib.webframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequestParameter {

    String name() default "";
    boolean required() default false;
    boolean fromUrl() default false;

}