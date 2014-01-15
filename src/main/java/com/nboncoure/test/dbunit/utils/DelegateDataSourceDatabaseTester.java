package com.nboncoure.test.dbunit.utils;

import javax.sql.DataSource;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DelegateDataSourceDatabaseTester.
 */
public class DelegateDataSourceDatabaseTester extends AbstractDatabaseTester {

    /** Logger. */
    static Logger logger = LoggerFactory
            .getLogger(DelegateDataSourceDatabaseTester.class);

    /** The data source. */
    private DataSource dataSource;

    /**
     * Creates a new DelegateDataSourceDatabaseTester with the specified
     * DataSource.
     * 
     * @param dataSource
     *            the DataSource to pull connections from
     */
    public DelegateDataSourceDatabaseTester(final DataSource dataSource) {
        super();

        if (dataSource == null) {
            throw new NullPointerException(
                    "The parameter 'dataSource' must not be null");
        }
        this.dataSource = dataSource;
    }

    /**
     * Creates a new DelegateDataSourceDatabaseTester with the specified
     * DataSource and schema name.
     * 
     * @param dataSource
     *            the DataSource to pull connections from
     * @param schema
     *            The schema name to be used for new dbunit connections
     * @since 2.4.5
     */
    public DelegateDataSourceDatabaseTester(final DataSource dataSource,
            final String schema) {
        super(schema);
        assertTrue("DataSource is not set", dataSource != null);

        if (dataSource == null) {
            throw new NullPointerException(
                    "The parameter 'dataSource' must not be null");
        }
        this.dataSource = dataSource;
    }

    @Override
    public IDatabaseConnection getConnection() throws Exception {
        logger.debug("getConnection() - start");

        return new DelegateDatabaseDataSourceConnection(dataSource, getSchema());
    }
}
