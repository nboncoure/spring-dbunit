package com.nboncoure.test.dbunit.initializer;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import com.nboncoure.test.dbunit.annotation.DBOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Base class for DatabaseInitializer based on DBUnit {@link IDataSet}
 */
public abstract class AbstractDataSetDatabaseInitializer implements
        DatabaseInitializer, ResourceLoaderAware, InitializingBean {

    /** The logger. */
    private Logger logger = LoggerFactory
            .getLogger(AbstractDataSetDatabaseInitializer.class);

    /** The resource loader. */
    private ResourceLoader resourceLoader;

    /** The location. */
    private String location;

    /** The data set. */
    private IDataSet dataSet;

    /** The connection. */
    @Autowired
    private IDatabaseConnection connection;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Set the location if the the resource.
     * 
     * @param pattern
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public IDatabaseConnection getConnection() {
        return connection;
    }

    /**
     * Sets the connection.
     * 
     * @param connection
     *            the new connection
     */
    public void setConnection(final IDatabaseConnection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.nboncoure.test.dbunit.initializer.DatabaseInitializer#initDatabase()
     */
    @Override
    public void initDatabase() throws Exception {
        DBOperation.INSERT.getDbunitOperation().execute(connection, dataSet);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        final Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            throw new BeanCreationException(
                    "No resource found at specified location : " + location);
        }
        logger.debug("Loading dataSet from resource : {}", resource.toString());
        this.dataSet = loadDataSet(resource);
    }

    protected abstract IDataSet loadDataSet(Resource resource) throws Exception;

}
