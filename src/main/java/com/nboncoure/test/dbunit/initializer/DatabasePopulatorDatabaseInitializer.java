package com.nboncoure.test.dbunit.initializer;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.Assert;

/**
 * A {@link DatabaseInitializer} implementation based on Spring
 * {@link DatabasePopulator}, to perform initialization using SQL scripts.
 */
public class DatabasePopulatorDatabaseInitializer implements
        DatabaseInitializer {

    /** The data source. */
    private DataSource dataSource;

    /** The database populator. */
    private DatabasePopulator databasePopulator;

    /**
     * Gets the data source.
     * 
     * @return the data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the data source.
     * 
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the database populator.
     * 
     * @return the database populator
     */
    public DatabasePopulator getDatabasePopulator() {
        return databasePopulator;
    }

    /**
     * Sets the database populator.
     * 
     * @param databasePopulator
     *            the new database populator
     */
    public void setDatabasePopulator(final DatabasePopulator databasePopulator) {
        this.databasePopulator = databasePopulator;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.nboncoure.test.dbunit.initializer.DatabaseInitializer#initDatabase()
     */
    @Override
    public void initDatabase() throws Exception {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            databasePopulator.populate(connection);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Inits the.
     * 
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
        Assert.notNull(dataSource, "A dataSource is required");
        Assert.notNull(databasePopulator, "A databasePopulator is required");
    }
}
