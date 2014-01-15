package com.nboncoure.test.dbunit.resolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import com.nboncoure.test.dbunit.annotation.DBOperation;
import com.nboncoure.test.dbunit.config.DbUnitConfiguration;
import com.nboncoure.test.dbunit.initializer.DatabaseInitializer;
import com.nboncoure.test.dbunit.utils.DBUnitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DataSetResolver} who resolves datasets using
 * {@link DatabaseInitializer}
 */
public class InitializerDataSetResolver implements DataSetResolver {

    private Logger logger = LoggerFactory
            .getLogger(InitializerDataSetResolver.class);

    /**
     * Cache for initialized datasets, ready to inject again.
     */
    private Map<String, IDataSet> cachedDataSets = new HashMap<String, IDataSet>();

    private Map<String, DatabaseInitializer> initializers;

    private DbUnitConfiguration configuration;

    public InitializerDataSetResolver(final DbUnitConfiguration configuration,
            final Map<String, DatabaseInitializer> initializers) {
        this.configuration = configuration;
        this.initializers = initializers;
    }

    @Override
    public IDataSet resolveDataSet(final String dataSetName) throws Exception {
        if (!cachedDataSets.containsKey(dataSetName)) {
            if (initializers != null && initializers.containsKey(dataSetName)) {
                final IDataSet dataSet = DBUnitUtils
                        .filter(configuration.getDatabaseConnection()
                                .createDataSet(), configuration);
                DBOperation.DELETE_ALL.getDbunitOperation().execute(
                        configuration.getDatabaseConnection(), dataSet);
                initializers.get(dataSetName).initDatabase();
            } else {
                return null;
            }

            IDataSet dataSet = configuration.getDatabaseConnection()
                    .createDataSet();
            dataSet = DBUnitUtils.filter(dataSet, configuration);
            try {
                // Write to memory byte array
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FlatXmlDataSet.write(dataSet, baos);

                // Rebuild DataSet from memory byte array and put in cache
                final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                builder.setCaseSensitiveTableNames(true);
                builder.setColumnSensing(true);
                builder.setDtdMetadata(true);

                dataSet = builder.build(new ByteArrayInputStream(baos
                        .toByteArray()));
                cachedDataSets.put(dataSetName, dataSet);
                logger.info("DataSet added in cache : '{}'.", dataSetName);
            } catch (final DataSetException e) {
                logger.error("unable to generate dataset", e);
            } catch (final IOException e) {
                logger.error("unable to generate dataset", e);
            }
        }
        return cachedDataSets.get(dataSetName);
    }
}
