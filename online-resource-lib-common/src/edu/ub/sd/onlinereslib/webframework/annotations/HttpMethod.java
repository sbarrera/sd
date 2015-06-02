package edu.ub.sd.onlinereslib.webframework.annotations;

import edu.ub.sd.onlinereslib.webframework.ServletDispatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    String type();
    String action() default ServletDispatcher.DEFAULT;

}
