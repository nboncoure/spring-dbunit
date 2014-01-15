package com.nboncoure.test.dbunit.annotation;

import org.junit.runners.model.InitializationError;
import com.nboncoure.test.dbunit.DbUnitTestContextManager;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test runner that preconfigure test execution listeners for DBunit + Spring
 * support
 */
public class DbUnitSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    /**
     * Instantiates a new db unit spring j unit4 class runner.
     * 
     * @param clazz
     *            the clazz
     * @throws InitializationError
     *             the initialization error
     */
    public DbUnitSpringJUnit4ClassRunner(final Class<?> clazz)
            throws InitializationError {
        super(clazz);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner#createTestContextManager(java.lang.Class)
     */
    @Override
    protected TestContextManager createTestContextManager(final Class<?> clazz) {
        return new DbUnitTestContextManager(clazz,
                getDefaultContextLoaderClassName(clazz));
    }
}
