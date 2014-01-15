package com.nboncoure.test.dbunit;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.nboncoure.test.dbunit.annotation.DbUnitSpringJUnit4ClassRunner;
import com.nboncoure.test.dbunit.annotation.InjectDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * The Class InjectOnClassTestCase.
 */
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:application-context.xml", "classpath:hsql-test-context.xml" })
@TransactionConfiguration(defaultRollback = false)
@InjectDataSet(value = "dataset", onceForClass = false)
@RunWith(DbUnitSpringJUnit4ClassRunner.class)
public class InjectOnClassTestCase {

    /** The database tester. */
    @Autowired
    private IDatabaseTester databaseTester;

    /** The data source. */
    @Autowired
    private DataSource dataSource;

    /**
     * Should have injected data set.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void shouldHaveInjectedDataSet() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table2");
        Assert.assertEquals(1, table1.getRowCount());
    }

    /**
     * Insert data in table2.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void insertDataInTable2() throws Exception {
        final JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute("insert into table2 values (4)");
        final ITable table1 = databaseTester.getConnection().createTable(
                "table2");
        Assert.assertEquals(2, table1.getRowCount());
    }

    /**
     * Should have resest data set.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void shouldHaveResestDataSet() throws Exception {
        final ITable table1 = databaseTester.getConnection().createTable(
                "table2");
        Assert.assertEquals(1, table1.getRowCount());
    }
}
