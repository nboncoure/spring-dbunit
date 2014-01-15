package com.nboncoure.test.dbunit.resolver;

import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.springframework.util.Assert;

/**
 * A {@link DataSetResolver} that delegates resolution to child resolvers
 */
public class CompositeDataSetResolver implements DataSetResolver {

    private List<DataSetResolver> resolvers;

    public CompositeDataSetResolver(final List<DataSetResolver> resolvers) {
        Assert.notEmpty(resolvers, "At least one resolver must be provided");
        this.resolvers = resolvers;
    }

    @Override
    public IDataSet resolveDataSet(final String dataSetName) throws Exception {
        for (final DataSetResolver resolver : resolvers) {
            final IDataSet dataSet = resolver.resolveDataSet(dataSetName);
            if (dataSet != null) {
                return dataSet;
            }
        }
        return null;
    }
}
