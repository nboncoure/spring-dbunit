package com.nboncoure.test.dbunit.namespace;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Test;
import com.nboncoure.test.dbunit.AbstractDBUnitTestCase;
import com.nboncoure.test.dbunit.config.DbUnitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class H2NamespaceTestCase.
 */
@ContextConfiguration(locations = { "classpath:namespace/test-namespace-context.xml" })
public class H2NamespaceTestCase extends AbstractDBUnitTestCase {

    /** The database tester. */
    @Autowired
    protected IDatabaseTester databaseTester;

    /** The database connection. */
    @Autowired
    protected IDatabaseConnection databaseConnection;

    /** The db unit configuration. */
    @Autowired
    protected DbUnitConfiguration dbUnitConfiguration;

    /**
     * Configuration should be not null.
     */
    @Test
    public void configurationShouldBeNotNull() {
        Assert.assertNotNull(dbUnitConfiguration);
    }

    /**
     * Connection should be not null.
     */
    @Test
    public void connectionShouldBeNotNull() {
        Assert.assertNotNull(databaseConnection);
    }

    /**
     * Tester should be not null.
     */
    @Test
    public void testerShouldBeNotNull() {
        Assert.assertNotNull(databaseTester);
    }

    /**
     * Should have database connection.
     */
    @Test
    public void shouldHaveDatabaseConnection() {
        Assert.assertNotNull(dbUnitConfiguration.getDatabaseConnection());
    }

    /**
     * Should have database tester.
     */
    @Test
    public void shouldHaveDatabaseTester() {
        Assert.assertNotNull(dbUnitConfiguration.getDatabaseTester());
    }
}
