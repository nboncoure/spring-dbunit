package com.nboncoure.test.dbunit.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.DefaultTableFilter;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.filter.IncludeTableFilter;
import com.nboncoure.test.dbunit.annotation.CleanupDB;
import com.nboncoure.test.dbunit.annotation.DBOperation;
import com.nboncoure.test.dbunit.annotation.InjectDataSet;
import com.nboncoure.test.dbunit.config.DbUnitConfiguration;
import com.nboncoure.test.dbunit.resolver.CompositeDataSetResolver;
import com.nboncoure.test.dbunit.resolver.DataSetResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Extracts a DBUnit flat XML dataset from a database.
 */
public class DBUnitUtils {

    private static Logger logger = LoggerFactory.getLogger(DBUnitUtils.class);

    /**
     * Resolver to use, not injected
     */
    private DataSetResolver dataSetResolver;

    public DBUnitUtils(final DataSetResolver resolver) {
        this(Arrays.asList(resolver));
    }

    public DBUnitUtils(final DataSetResolver... resolvers) {
        this(Arrays.asList(resolvers));
    }

    public DBUnitUtils(final List<DataSetResolver> resolvers) {
        Assert.notEmpty(resolvers, "At least one resolver must be provided");
        if (resolvers.size() == 1) {
            dataSetResolver = resolvers.get(0);
        } else {
            dataSetResolver = new CompositeDataSetResolver(resolvers);
        }

    }

    /**
     * Performs a cleanup operation. If no tables filters are specified in
     * configuration, data from entire database will be cleanup. Otherwise, a
     * partial cleanup will be performed.
     * 
     * @throws Exception
     */
    public void cleanup(final DbUnitConfiguration configuration,
            final CleanupDB cleanupDB) throws Exception {
        final IDataSet dataSet = filter(configuration.getDatabaseConnection()
                .createDataSet(), configuration);
        DBOperation.DELETE_ALL.getDbunitOperation().execute(
                configuration.getDatabaseConnection(), dataSet);
        // configuration.getDatabaseConnection().close();
        logger.info("Cleanup completed");
    }

    /**
     * Performs a inject operation. If no tables filters are specified in
     * configuration, the whole dataset is injected. Otherwise, only a subset
     * will be injected.
     * 
     * @throws Exception
     */
    public void inject(final DbUnitConfiguration configuration,
            final InjectDataSet injectDataSet) throws Exception {

        if (injectDataSet.value() == null || injectDataSet.value().length == 0) {
            // nothing to inject
            return;
        }

        IDataSet idataSet;
        if (injectDataSet.value().length == 1) {
            final String datasetLocation = injectDataSet.value()[0];
            logger.info("Inject dataset '{}'.", datasetLocation);
            idataSet = createDataSet(datasetLocation, configuration);
        } else {
            final List<IDataSet> datasets = new ArrayList<IDataSet>();
            logger.info("Inject datasets : {}",
                    Arrays.toString(injectDataSet.value()));
            for (final String datasetLocation : injectDataSet.value()) {
                datasets.add(createDataSet(datasetLocation, configuration));
            }
            idataSet = new CompositeDataSet(
                    datasets.toArray(new IDataSet[datasets.size()]));
        }
        idataSet = filter(idataSet, configuration);
        injectDataSet.dbOperation().getDbunitOperation()
                .execute(configuration.getDatabaseConnection(), idataSet);
        logger.info("Injection completed");
    }

    private IDataSet createDataSet(final String datasetName,
            final DbUnitConfiguration configuration) throws Exception {
        final IDataSet dataSet = dataSetResolver.resolveDataSet(datasetName);
        if (dataSet == null) {
            throw new IllegalArgumentException(
                    "Unable to resolve dataset named : " + datasetName);
        }
        return dataSet;
    }

    /**
     * Filters the dataset according to includes and excludes in the
     * configuration.
     * 
     * @param source
     * @param configuration
     * @return
     */
    public static IDataSet filter(final IDataSet source,
            final DbUnitConfiguration configuration) {
        final List<String> excludeTables = configuration.getExcludeTables();
        final List<String> includeTables = configuration.getIncludeTables();
        IDataSet dataSetToUse;
        final boolean hasExcludeTables = !CollectionUtils
                .isEmpty(excludeTables);
        final boolean hasIncludeTables = !CollectionUtils
                .isEmpty(includeTables);
        if (hasExcludeTables || hasIncludeTables) {
            ITableFilter tableFilter;
            if (hasExcludeTables && hasIncludeTables) {
                final DefaultTableFilter defaultTableFilter = new DefaultTableFilter();
                for (final String table : includeTables) {
                    defaultTableFilter.includeTable(table);
                }
                for (final String table : excludeTables) {
                    defaultTableFilter.excludeTable(table);
                }
                tableFilter = defaultTableFilter;
            } else if (hasExcludeTables) {
                tableFilter = new ExcludeTableFilter(
                        excludeTables.toArray(new String[excludeTables.size()]));
            } else {
                tableFilter = new IncludeTableFilter(
                        includeTables.toArray(new String[includeTables.size()]));
            }

            dataSetToUse = new FilteredDataSet(tableFilter, source);
        } else {
            dataSetToUse = source;
        }
        return dataSetToUse;
    }
}
