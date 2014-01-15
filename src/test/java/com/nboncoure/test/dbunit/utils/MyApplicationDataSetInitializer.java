package com.nboncoure.test.dbunit.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import com.nboncoure.test.dbunit.initializer.DatabaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class MyApplicationDataSetInitializer.
 */
public class MyApplicationDataSetInitializer implements DatabaseInitializer {

    /** The Constant NB_ROWS. */
    public static final int NB_ROWS = 10;

    /** The Constant START_INDEX. */
    public static final int START_INDEX = 10;

    /** The data source. */
    @Autowired
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     * 
     * @see com.nboncoure.test.dbunit.initializer.DatabaseInitializer#initDatabase()
     */
    @Override
    public void initDatabase() throws Exception {
        final Connection connection = dataSource.getConnection();
        final PreparedStatement table1Statement = connection
                .prepareStatement("insert into table2 values (?)");
        final PreparedStatement table2Statement = connection
                .prepareStatement("insert into table1 values (?,?)");
        try {
            for (int i = 0; i < NB_ROWS; i++) {
                final int actualIndex = i + START_INDEX;
                table1Statement.setInt(1, actualIndex);
                table1Statement.execute();
                table2Statement.setInt(1, actualIndex);
                table2Statement.setInt(2, actualIndex);
                table2Statement.execute();
            }
        } finally {
            table1Statement.close();
            table2Statement.close();
            connection.close();
        }
    }
}
