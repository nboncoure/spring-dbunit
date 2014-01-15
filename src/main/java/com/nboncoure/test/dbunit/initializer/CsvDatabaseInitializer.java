package com.nboncoure.test.dbunit.initializer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.springframework.core.io.Resource;

/**
 * DatabaseInitializer implementation based on DBUnit {@link CsvDataSet}
 */
public class CsvDatabaseInitializer extends AbstractDataSetDatabaseInitializer {

    /**
     * {@inheritDoc}
     * 
     * @see com.nboncoure.test.dbunit.initializer.AbstractDataSetDatabaseInitializer#loadDataSet(org.springframework.core.io.Resource)
     */
    @Override
    protected IDataSet loadDataSet(final Resource resource) throws Exception {
        return new CsvDataSet(resource.getFile());
    }
}
