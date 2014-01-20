# spring-dbuint 2.0.0

## Overview

spring-dbunit provides [DBUnit](http://www.dbunit.org) add-ons for Spring.

It allows you to easily insert and cleanup test dataset into the database of your choice.

## Usage

Add some annotations on your class test to inject your dataset
	
	import com.nboncoure.test.dbunit.annotation.DbUnitSpringJUnit4ClassRunner;
	import com.nboncoure.test.dbunit.annotation.InjectDataSet;
	
	@RunWith(DbUnitSpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = { "classpath:test-context.xml" })
	@TransactionConfiguration(defaultRollback = false)
	@Transactional(readOnly = false)
	@InjectDataSet(value = "foo-dataset", onceForClass = false)
	public class FooRepositoryTest {
	...
	}

Add the dataset in your context definition (test-context.xml)

	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:p="http://www.springframework.org/schema/p"
		xmlns:jdbc="http://www.springframework.org/schema/jdbc"
		xmlns:dbunit="http://www.didrok.com/schema/dbunit"
		xsi:schemaLocation="http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
			http://www.nboncoure.com/schema/dbunit http://www.nboncoure.com/schema/dbunit/didrok-dbunit.xsd
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
		<dbunit:configuration data-source="dataSource"/>
		<bean id="foo-dataset"
			class="com.nboncoure.test.dbunit.initializer.XMLDatabaseInitializer"
			p:location="classpath:datasets/foo-dataset.xml" />
		...
	</beans>

## Changes logs

### 2.0.0

Usable with version 4 of Spring.