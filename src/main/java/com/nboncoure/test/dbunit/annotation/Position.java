package com.nboncoure.test.dbunit.annotation;

/**
 * Describes where the action must take place.
 */
public enum Position {

    /**
     * Executes before test class or method
     */
    BEFORE,

    /**
     * Executes after test class or method
     */
    AFTER,

    /**
     * Executes before and after test class or method
     */
    BOTH;

}
