package com.blogitory.blog.commons.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restrict access to anonymous only content.
 *
 * @author woonseok
 * @since 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAnonymous {
}
