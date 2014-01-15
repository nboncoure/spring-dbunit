package com.nboncoure.test.dbunit.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler} for the '
 * <code>dbunit-test</code>' namespace.
 */
public class DBUnitNamespaceHandler extends NamespaceHandlerSupport {

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("configuration",
                new DBUnitConfigurationParser());
    }
}
