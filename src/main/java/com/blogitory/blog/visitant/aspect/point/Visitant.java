package com.blogitory.blog.visitant.aspect.point;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Visitant annotation for AOP.
 *
 * @author woonseok
 * @since 1.0
 **/
@Target(value = ElementType.METHOD)
public @interface Visitant {
}
