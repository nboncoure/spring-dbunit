package com.nboncoure.test.dbunit.initializer;

/**
 * The Interface DatabaseInitializer.
 */
public interface DatabaseInitializer {

    /**
     * Subclasses must populate the database in this method.
     * <p>
     * The database is automatically cleared before this method is executed.
     * </p>
     * 
     * @throws Exception
     */
    void initDatabase() throws Exception;
}
