package com.blogitory.blog.commons.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restrict access to anonymous only content.
 *
 * @author woonseok
 * @Date 2024-07-24
 * @since 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAnonymous {
}
