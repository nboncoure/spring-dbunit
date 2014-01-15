package com.nboncoure.test.dbunit.config;

import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.database.IDatabaseConnection;
import com.nboncoure.test.dbunit.utils.DelegateDatabaseDataSourceConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * Factory bean for {@link IDatabaseConnection}. It creates an instance of
 * {@link DelegateDatabaseDataSourceConnection} to perform automatic
 * configuration depending on the detected database.
 */
public class DatabaseConnectionFactory implements
        FactoryBean<IDatabaseConnection> {

    /** The logger. */
    static Logger logger = LoggerFactory
            .getLogger(DatabaseConnectionFactory.class);

    /** The db unit properties. */
    private Map<String, Object> dbUnitProperties;

    /** The data source. */
    private DataSource dataSource;

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
     * Gets the db unit properties.
     * 
     * @return the db unit properties
     */
    public Map<String, Object> getDbUnitProperties() {
        return dbUnitProperties;
    }

    /**
     * Optionally set the DBUnit properties to use. see <a
     * href="http://www.dbunit.org/properties.html">dunit documentation</a> for
     * more information about valid properties.
     * 
     * @param dbUnitProperties
     */
    public void setDbUnitProperties(final Map<String, Object> dbUnitProperties) {
        this.dbUnitProperties = dbUnitProperties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public IDatabaseConnection getObject() throws Exception {
        Assert.notNull(dataSource, "dataSource is required");

        final DelegateDatabaseDataSourceConnection databaseConnection = new DelegateDatabaseDataSourceConnection(
                dataSource);

        if (dbUnitProperties != null && !dbUnitProperties.isEmpty()) {
            for (final Map.Entry<String, Object> entry : dbUnitProperties
                    .entrySet()) {
                databaseConnection.getConfig().setProperty(entry.getKey(),
                        entry.getValue());
            }
        }
        return databaseConnection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return IDatabaseConnection.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

}
