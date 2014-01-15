package com.nboncoure.test.dbunit.config;

import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.IncludeTableFilter;

/**
 * Stores DBunit configuration.
 */
public class DbUnitConfiguration {

    /** The database connection. */
    private IDatabaseConnection databaseConnection;

    /** The database tester. */
    private IDatabaseTester databaseTester;

    /** The exclude tables. */
    private List<String> excludeTables;

    /** The include tables. */
    private List<String> includeTables;

    /**
     * Gets the database connection.
     * 
     * @return the database connection
     */
    public IDatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    /**
     * Sets the database connection.
     * 
     * @param databaseConnection
     *            the new database connection
     */
    public void setDatabaseConnection(
            final IDatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Gets the database tester.
     * 
     * @return the database tester
     */
    public IDatabaseTester getDatabaseTester() {
        return databaseTester;
    }

    /**
     * Sets the database tester.
     * 
     * @param databaseTester
     *            the new database tester
     */
    public void setDatabaseTester(final IDatabaseTester databaseTester) {
        this.databaseTester = databaseTester;
    }

    /**
     * Gets the exclude tables.
     * 
     * @return the exclude tables
     */
    public List<String> getExcludeTables() {
        return excludeTables;
    }

    /**
     * Set the table list to exclude during dataset injection or extraction.
     * Accepted values are either a table name or a pattern including * and ?
     * characters.
     * 
     * @param excludeTables
     *            the new exclude tables
     * @see ExcludeTableFilter
     */
    public void setExcludeTables(final List<String> excludeTables) {
        this.excludeTables = excludeTables;
    }

    /**
     * Gets the include tables.
     * 
     * @return the include tables
     */
    public List<String> getIncludeTables() {
        return includeTables;
    }

    /**
     * Set the table list to include during dataset injection or extraction.
     * Accepted values are either a table name or a pattern including * and ?
     * characters.
     * 
     * @param includeTables
     *            the new include tables
     * @see IncludeTableFilter
     */
    public void setIncludeTables(final List<String> includeTables) {
        this.includeTables = includeTables;
    }
}
