package com.blogitory.blog.viewer.aspect.point;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Viewer annotation for AOP.
 *
 * @author woonseok
 * @since 1.0
 **/
@Target(value = ElementType.METHOD)
public @interface Viewer {
}
