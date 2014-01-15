package com.nboncoure.test.dbunit.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.ext.oracle.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DelegateDatabaseDataSourceConnection.
 */
public class DelegateDatabaseDataSourceConnection implements
        IDatabaseConnection {

    /** Logger. */
    static Logger logger = LoggerFactory
            .getLogger(DelegateDatabaseDataSourceConnection.class);

    /** The database connection. */
    private DatabaseConnection databaseConnection;

    /** The disable fk command. */
    private String disableFkCommand = null;

    /** The enable fk command. */
    private String enableFkCommand = null;

    /** The connection. */
    private Connection connection;

    /** The data source. */
    private DataSource dataSource;

    /**
     * Instantiates a new delegate database data source connection.
     * 
     * @param dataSource
     *            the data source
     * @throws SQLException
     *             the sQL exception
     * @throws DatabaseUnitException
     *             the database unit exception
     */
    public DelegateDatabaseDataSourceConnection(final DataSource dataSource)
            throws SQLException, DatabaseUnitException {
        this(dataSource, null);
    }

    /**
     * Instantiates a new delegate database data source connection.
     * 
     * @param dataSource
     *            the data source
     * @param schema
     *            the schema
     * @throws SQLException
     *             the sQL exception
     * @throws DatabaseUnitException
     *             the database unit exception
     */
    public DelegateDatabaseDataSourceConnection(final DataSource dataSource,
            final String schema) throws SQLException, DatabaseUnitException {
        this.dataSource = dataSource;
        final Connection connection = dataSource.getConnection();
        final DatabaseMetaData metatData = connection.getMetaData();
        final String productName = metatData.getDatabaseProductName()
                .toLowerCase();

        if (productName.contains("hsql")) {
            logger.info("Detected database : hsql");
            databaseConnection = new HsqldbConnection(connection, null);
            disableFkCommand = "SET DATABASE REFERENTIAL INTEGRITY FALSE;";
            enableFkCommand = "SET DATABASE REFERENTIAL INTEGRITY TRUE;";
        } else if (productName.contains("mysql")) {
            logger.info("Detected database : mysql");
            databaseConnection = new MySqlConnection(connection, null);
            disableFkCommand = "SET foreign_key_checks = 0;";
            enableFkCommand = "SET foreign_key_checks = 1;";
        } else if (productName.contains("oracle")) {
            logger.info("Detected database : oracle");
            databaseConnection = new OracleConnection(connection, null);
            // TODO disable FK for oracle
        } else if (productName.contains("h2")) {
            logger.info("Detected database : h2");
            databaseConnection = new H2Connection(connection, null);
            disableFkCommand = "SET REFERENTIAL_INTEGRITY FALSE";
            enableFkCommand = "SET REFERENTIAL_INTEGRITY TRUE";
        } else {
            logger.info("No specific database detected, using default");
            databaseConnection = new DatabaseConnection(connection, null);
        }
        databaseConnection.getConfig().setProperty(
                DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            if (disableFkCommand != null) {
                final Statement statement = connection.createStatement();
                try {
                    statement.execute(disableFkCommand);
                } finally {
                    statement.close();
                }
            }
        }
        return connection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#close()
     */
    @Override
    public void close() throws SQLException {
        if (connection != null) {
            if (enableFkCommand != null) {
                final Statement statement = connection.createStatement();
                try {
                    statement.execute(enableFkCommand);
                } finally {
                    statement.close();
                }
            }
            connection.close();
            connection = null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getSchema()
     */
    @Override
    public String getSchema() {
        return databaseConnection.getSchema();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#createDataSet()
     */
    @Override
    public IDataSet createDataSet() throws SQLException {
        return databaseConnection.createDataSet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#createDataSet(java.lang.String[])
     */
    @Override
    public IDataSet createDataSet(final String[] tableNames)
            throws DataSetException, SQLException {
        return databaseConnection.createDataSet(tableNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#createQueryTable(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ITable createQueryTable(final String resultName, final String sql)
            throws DataSetException, SQLException {
        return databaseConnection.createQueryTable(resultName, sql);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#createTable(java.lang.String,
     *      java.sql.PreparedStatement)
     */
    @Override
    public ITable createTable(final String resultName,
            final PreparedStatement preparedStatement) throws DataSetException,
            SQLException {
        return databaseConnection.createTable(resultName, preparedStatement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#createTable(java.lang.String)
     */
    @Override
    public ITable createTable(final String tableName) throws DataSetException,
            SQLException {
        return databaseConnection.createTable(tableName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return databaseConnection.equals(obj);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getConfig()
     */
    @Override
    public DatabaseConfig getConfig() {
        return databaseConnection.getConfig();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getRowCount(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public int getRowCount(final String tableName, final String whereClause)
            throws SQLException {
        return databaseConnection.getRowCount(tableName, whereClause);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getRowCount(java.lang.String)
     */
    @Override
    public int getRowCount(final String tableName) throws SQLException {
        return databaseConnection.getRowCount(tableName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.dbunit.database.IDatabaseConnection#getStatementFactory()
     */
    @Override
    @SuppressWarnings("deprecation")
    public IStatementFactory getStatementFactory() {
        return databaseConnection.getStatementFactory();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return databaseConnection.hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return databaseConnection.toString();
    }
}
