package com.nboncoure.test.dbunit.namespace;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class H2NamespaceIncludesTestCase.
 */
@ContextConfiguration(locations = { "classpath:namespace/test-namespace-includes-context.xml" })
public class H2NamespaceIncludesTestCase extends H2NamespaceTestCase {

    /**
     * Should have includes.
     */
    @Test
    public void shouldHaveIncludes() {
        Assert.assertNotNull(dbUnitConfiguration.getIncludeTables());
        Assert.assertEquals(2, dbUnitConfiguration.getIncludeTables().size());
    }

    /**
     * Should have excludes.
     */
    @Test
    public void shouldHaveExcludes() {
        Assert.assertNotNull(dbUnitConfiguration.getExcludeTables());
        Assert.assertEquals(1, dbUnitConfiguration.getExcludeTables().size());
    }
}
