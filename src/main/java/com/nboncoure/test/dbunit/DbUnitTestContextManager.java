package com.nboncoure.test.dbunit;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;

/**
 * The Class DbUnitTestContextManager.
 */
public class DbUnitTestContextManager extends TestContextManager {

    /** Logger. */
    static Logger logger = LoggerFactory
            .getLogger(DbUnitTestContextManager.class);

    /** The Constant DEFAULT_DBUNIT_TEST_EXECUTION_LISTENER_CLASS_NAMES. */
    private static final String[] DEFAULT_DBUNIT_TEST_EXECUTION_LISTENER_CLASS_NAMES = new String[] {
            "org.springframework.test.context.support.DependencyInjectionTestExecutionListener",
            "org.springframework.test.context.support.DirtiesContextTestExecutionListener",
            "com.nboncoure.test.dbunit.DbUnitTestExecutionListener",
            "org.springframework.test.context.transaction.TransactionalTestExecutionListener" };

    /**
     * Instantiates a new db unit test context manager.
     * 
     * @param testClass
     *            the test class
     */
    public DbUnitTestContextManager(final Class<?> testClass) {
        super(testClass);
    }

    /**
     * Instantiates a new db unit test context manager.
     * 
     * @param testClass
     *            the test class
     * @param defaultContextLoaderClassName
     *            the default context loader class name
     */
    public DbUnitTestContextManager(final Class<?> testClass,
            final String defaultContextLoaderClassName) {
        super(testClass, defaultContextLoaderClassName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.test.context.TestContextManager#getDefaultTestExecutionListenerClasses()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Set<Class<? extends TestExecutionListener>> getDefaultTestExecutionListenerClasses() {
        final Set<Class<? extends TestExecutionListener>> defaultListenerClasses = new LinkedHashSet<Class<? extends TestExecutionListener>>();
        for (final String className : DEFAULT_DBUNIT_TEST_EXECUTION_LISTENER_CLASS_NAMES) {
            try {
                defaultListenerClasses
                        .add((Class<? extends TestExecutionListener>) getClass()
                                .getClassLoader().loadClass(className));
            } catch (final Throwable ex) {
                logger.debug(
                        "Could not load default TestExecutionListener class [{}]. Specify custom listener classes or make the default listener classes available.",
                        className);
            }
        }
        return defaultListenerClasses;
    }
}
