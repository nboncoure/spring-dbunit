package com.nboncoure.test.dbunit.config;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.DefaultDatabaseTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * The Class DBUnitConfigurationParser.
 */
public class DBUnitConfigurationParser extends
        AbstractSingleBeanDefinitionParser {

    /** The logger. */
    static Logger logger = LoggerFactory
            .getLogger(DBUnitConfigurationParser.class);

    /** The Constant INCLUDE_TABLE_ELEMENT. */
    public static final String INCLUDE_TABLE_ELEMENT = "include-table";

    /** The Constant EXCLUDE_TABLE_ELEMENT. */
    public static final String EXCLUDE_TABLE_ELEMENT = "exclude-table";

    /** The Constant DATASOURCE_ATTRIBUTE. */
    public static final String DATASOURCE_ATTRIBUTE = "data-source";

    /**
     * Gets the bean class name.
     * 
     * @param element
     *            the element
     * @return the bean class name
     */
    @Override
    protected String getBeanClassName(final Element element) {
        return DbUnitConfiguration.class.getCanonicalName();
    }

    /**
     * Do parse.
     * 
     * @param element
     *            the element
     * @param parserContext
     *            the parser context
     * @param builder
     *            the builder
     */
    @Override
    protected void doParse(final Element element,
            final ParserContext parserContext,
            final BeanDefinitionBuilder builder) {

        final BeanDefinitionBuilder databaseConnectionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DatabaseConnectionFactory.class);
        databaseConnectionBuilder.addPropertyReference("dataSource",
                element.getAttribute(DATASOURCE_ATTRIBUTE));
        // TODO : add support for dbunitProperties
        final AbstractBeanDefinition databaseConnectionBean = databaseConnectionBuilder
                .getBeanDefinition();

        final String databaseConnectionBeanName = resolveId(element,
                databaseConnectionBean, parserContext) + "-databaseConnection";
        parserContext.getRegistry().registerBeanDefinition(
                databaseConnectionBeanName, databaseConnectionBean);
        builder.addPropertyReference("databaseConnection",
                databaseConnectionBeanName);

        final BeanDefinitionBuilder dbTesterBeanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DefaultDatabaseTester.class);
        dbTesterBeanDefinitionBuilder
                .addConstructorArgReference(databaseConnectionBeanName);
        final AbstractBeanDefinition dbTesterBean = dbTesterBeanDefinitionBuilder
                .getBeanDefinition();
        final String dbTesterBeanName = resolveId(element, dbTesterBean,
                parserContext) + "-databaseTester";
        parserContext.getRegistry().registerBeanDefinition(dbTesterBeanName,
                dbTesterBean);
        builder.addPropertyReference("databaseTester", dbTesterBeanName);

        final List<Element> includeElements = DomUtils
                .getChildElementsByTagName(element, INCLUDE_TABLE_ELEMENT);
        if (includeElements.size() > 0) {
            final List<String> includes = new ArrayList<String>();
            for (final Element includeElement : includeElements) {
                includes.add(includeElement.getNodeValue());
            }
            builder.addPropertyValue("includeTables", includes);
        }

        final List<Element> excludeElements = DomUtils
                .getChildElementsByTagName(element, EXCLUDE_TABLE_ELEMENT);
        if (excludeElements.size() > 0) {
            final List<String> excludes = new ArrayList<String>();
            for (final Element excludeElement : excludeElements) {
                excludes.add(excludeElement.getNodeValue());
            }
            builder.addPropertyValue("excludeTables", excludes);
        }

    }

    /**
     * Should generate id as fallback.
     * 
     * @return true, if successful
     */
    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }
}
