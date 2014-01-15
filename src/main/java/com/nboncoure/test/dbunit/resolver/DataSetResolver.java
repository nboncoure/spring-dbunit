package com.nboncoure.test.dbunit.resolver;

import org.dbunit.dataset.IDataSet;

/**
 * The Interface DataSetResolver.
 */
public interface DataSetResolver {

    /**
     * Resolve data set.
     * 
     * @param dataSetName
     *            the data set name
     * @return the inputStream if found, or null
     * @throws Exception
     *             the exception
     */
    IDataSet resolveDataSet(String dataSetName) throws Exception;
}
