package com.nboncoure.test.dbunit.initializer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

/**
 * DatabaseInitializer implementation based on DBUnit {@link FlatXmlDataSet}
 */
public class FlatXMLDatabaseInitializer extends
        AbstractDataSetDatabaseInitializer {

    /**
     * {@inheritDoc}
     * 
     * @see com.nboncoure.test.dbunit.initializer.AbstractDataSetDatabaseInitializer#loadDataSet(org.springframework.core.io.Resource)
     */
    @Override
    protected IDataSet loadDataSet(final Resource resource) throws Exception {
        final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setCaseSensitiveTableNames(true);
        builder.setColumnSensing(true);
        builder.setDtdMetadata(true);
        return builder.build(resource.getInputStream());
    }
}
