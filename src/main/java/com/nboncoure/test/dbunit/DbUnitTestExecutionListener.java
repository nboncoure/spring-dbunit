package com.nboncoure.test.dbunit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.nboncoure.test.dbunit.annotation.CleanupDB;
import com.nboncoure.test.dbunit.annotation.DbUnitContext;
import com.nboncoure.test.dbunit.annotation.InjectDataSet;
import com.nboncoure.test.dbunit.annotation.Position;
import com.nboncoure.test.dbunit.annotation.SkipInjection;
import com.nboncoure.test.dbunit.config.DbUnitConfiguration;
import com.nboncoure.test.dbunit.initializer.DatabaseInitializer;
import com.nboncoure.test.dbunit.resolver.DataSetResolver;
import com.nboncoure.test.dbunit.resolver.InitializerDataSetResolver;
import com.nboncoure.test.dbunit.utils.DBUnitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

/**
 * A <code>TestExecutionListener</code> who provides support for dbunit dataset
 * injection before test class and test methods. This
 * <code>TestExecutionListener</code> looks for the {@link InjectDataSet} and
 * {@link ExtractDataSet} annotations on test classes and methods.
 * {@link InjectDataSet} injects the appropriate dataset
 * <p>
 * <b>Important : this TestExecutionListener must be put after the
 * {@link TransactionalTestExecutionListener}</b>
 * </p>
 */
public class DbUnitTestExecutionListener implements TestExecutionListener {

    /** Logger. */
    static Logger logger = LoggerFactory
            .getLogger(DbUnitTestExecutionListener.class);

    private Set<Class<?>> injectedTestInstances;

    private DBUnitUtils dbUnitUtils;

    public DbUnitTestExecutionListener() {
        injectedTestInstances = new LinkedHashSet<Class<?>>();
    }

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        init(testContext);
        final CleanupDB cleanupDB = testContext.getTestMethod().getAnnotation(
                CleanupDB.class);
        if (cleanupDB != null
                && (cleanupDB.position() == Position.AFTER || cleanupDB
                        .position() == Position.BOTH)) {
            final DbUnitConfiguration configuration = getRequiredDbUnitConfiguration(testContext);
            dbUnitUtils.cleanup(configuration, cleanupDB);
        }

        // ExtractDataSet extractDataSet =
        // testContext.getTestMethod().getAnnotation(ExtractDataSet.class);
        // if (extractDataSet != null) {
        // if (testContext.getTestException() != null) {
        // LOGGER.info("Aborting extract because test have thrown an exception.");
        // return;
        // }
        // if (extractDataSet.value() == null) {
        // throw new
        // IllegalArgumentException("DataSet name is required on ExtractDataSet annotation");
        // }
        //
        // DbUnitConfiguration configuration =
        // testContext.getTestInstance().getClass().getAnnotation(DbUnitConfiguration.class);
        //
        // DBUnitUtils.extract(extractDataSet.value(),
        // getDatabaseConnection(testContext), extractDataSet.tables(),
        // extractDataSet
        // .queries(), configuration.ignoredTables());
        //
        // LOGGER.info("Completed extraction to '{}'.", extractDataSet.value());
        // }
    }

    @Override
    public void beforeTestMethod(final TestContext testContext)
            throws Exception {
        init(testContext);
        final CleanupDB cleanupDB = testContext.getTestMethod().getAnnotation(
                CleanupDB.class);
        if (cleanupDB != null
                && (cleanupDB.position() == Position.BEFORE || cleanupDB
                        .position() == Position.BOTH)) {
            final DbUnitConfiguration configuration = getRequiredDbUnitConfiguration(testContext);
            dbUnitUtils.cleanup(configuration, cleanupDB);
        }

        final SkipInjection skipInjection = testContext.getTestMethod()
                .getAnnotation(SkipInjection.class);
        if (skipInjection != null) {
            return;
        }
        InjectDataSet injectDataSet = testContext.getTestMethod()
                .getAnnotation(InjectDataSet.class);
        if (injectDataSet == null) {
            // look for annotation on class
            final InjectDataSet injectDataSetOnClass = testContext
                    .getTestInstance().getClass()
                    .getAnnotation(InjectDataSet.class);
            if (injectDataSetOnClass != null
                    && injectDataSetOnClass.onceForClass()
                    && injectedTestInstances.contains(testContext
                            .getTestInstance().getClass())) {
                logger.debug("Test instance already initialized, skipping. {}",
                        testContext.getTestInstance());
            } else {
                injectDataSet = injectDataSetOnClass;
            }
            injectedTestInstances.add(testContext.getTestInstance().getClass());
        }
        if (injectDataSet != null) {
            dbUnitUtils.inject(getRequiredDbUnitConfiguration(testContext),
                    injectDataSet);
        }
    }

    @Override
    public void prepareTestInstance(final TestContext testContext)
            throws Exception {

    }

    @Override
    public void afterTestClass(final TestContext testContext) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        // Nothing to do
    }

    private DbUnitConfiguration getRequiredDbUnitConfiguration(
            final TestContext testContext) {
        final DbUnitContext contextAnnotation = testContext.getTestInstance()
                .getClass().getAnnotation(DbUnitContext.class);

        DbUnitConfiguration configuration;

        if (contextAnnotation != null
                && StringUtils.hasText(contextAnnotation.value())) {
            configuration = testContext.getApplicationContext().getBean(
                    contextAnnotation.value(), DbUnitConfiguration.class);
        } else {
            // take any bean of type DbUnitConfiguration
            configuration = testContext.getApplicationContext().getBean(
                    DbUnitConfiguration.class);
        }

        return configuration;
    }

    private void init(final TestContext testContext) {
        if (dbUnitUtils == null) {
            final Collection<DataSetResolver> resolvers = testContext
                    .getApplicationContext()
                    .getBeansOfType(DataSetResolver.class).values();
            if (!resolvers.isEmpty()) {
                dbUnitUtils = new DBUnitUtils(new ArrayList<DataSetResolver>(
                        resolvers));
            } else {
                final Map<String, DatabaseInitializer> initializers = testContext
                        .getApplicationContext().getBeansOfType(
                                DatabaseInitializer.class);
                final InitializerDataSetResolver resolver = new InitializerDataSetResolver(
                        getRequiredDbUnitConfiguration(testContext),
                        initializers);
                dbUnitUtils = new DBUnitUtils(resolver);
            }

        }
    }
}
