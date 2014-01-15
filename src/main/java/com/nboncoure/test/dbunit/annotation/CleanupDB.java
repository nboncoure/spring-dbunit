package com.nboncoure.test.dbunit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation can be used on a JUnit test class or method in order to
 * cleanup the database before a test.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CleanupDB {

    /**
     * Tables.
     * 
     * @return the string[]
     */
    String[] tables() default {};

    /**
     * Position.
     * 
     * @return the position
     */
    Position position() default Position.BEFORE;

}
