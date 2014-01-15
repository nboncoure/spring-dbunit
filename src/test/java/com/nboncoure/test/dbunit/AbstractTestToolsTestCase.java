package com.nboncoure.test.dbunit;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import com.nboncoure.test.dbunit.annotation.DBOperation;
import com.nboncoure.test.dbunit.annotation.InjectDataSet;
import com.nboncoure.test.dbunit.utils.MyApplicationDataSetInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * The Class AbstractTestToolsTestCase.
 */
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:application-context.xml" })
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractTestToolsTestCase extends AbstractDBUnitTestCase {

    /** The database tester. */
    @Autowired
    private IDatabaseTester databaseTester;

    /**
     * Test xml data set.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    @InjectDataSet({ "dataset", "dataset2" })
    public void testXMLDataSet() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table1");
        Assert.assertEquals(2, table1.getRowCount());
    }

    /**
     * Test some other method.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    @InjectDataSet("datasetInitializer")
    public void testSomeOtherMethod() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table1");
        Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
                table1.getRowCount());
    }

    /**
     * Test some other method with same data set.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    @InjectDataSet("datasetInitializer")
    public void testSomeOtherMethodWithSameDataSet() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table1");
        Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
                table1.getRowCount());
    }

    /**
     * Test with same data set twice.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    @InjectDataSet(value = { "datasetInitializer", "datasetInitializer" }, dbOperation = DBOperation.REFRESH)
    public void testWithSameDataSetTwice() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table1");
        Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
                table1.getRowCount());
    }

    /**
     * Test with mixed data set types.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    @InjectDataSet(value = { "datasetInitializer", "dataset" })
    public void testWithMixedDataSetTypes() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table1");
        Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS + 1,
                table1.getRowCount());
    }
}
