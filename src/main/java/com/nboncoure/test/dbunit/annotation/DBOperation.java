package com.nboncoure.test.dbunit.annotation;

import org.dbunit.operation.DatabaseOperation;

/**
 * Defines what DBUnit operation must be performed.
 * <p>
 * Each corresponding DBUnit operation is surrounded by a
 * {@link DatabaseOperation#CLOSE_CONNECTION(DatabaseOperation)} and a
 * {@link DatabaseOperation#TRANSACTION(DatabaseOperation)} operation.
 * </p>
 * 
 * @see DatabaseOperation
 */
public enum DBOperation {

    /**
     * @see DatabaseOperation#NONE
     */
    NONE(DatabaseOperation.NONE),

    /**
     * @see DatabaseOperation#UPDATE
     */
    UPDATE(DatabaseOperation.UPDATE),

    /**
     * @see DatabaseOperation#INSERT
     */
    INSERT(DatabaseOperation.INSERT),

    /**
     * @see DatabaseOperation#REFRESH
     */
    REFRESH(DatabaseOperation.REFRESH),

    /**
     * @see DatabaseOperation#DELETE
     */
    DELETE(DatabaseOperation.DELETE),

    /**
     * @see DatabaseOperation#DELETE_ALL
     */
    DELETE_ALL(DatabaseOperation.DELETE_ALL),

    /**
     * @see DatabaseOperation#TRUNCATE_TABLE
     */
    TRUNCATE_TABLE(DatabaseOperation.TRUNCATE_TABLE),

    /**
     * @see DatabaseOperation#CLEAN_INSERT
     */
    CLEAN_INSERT(DatabaseOperation.CLEAN_INSERT);

    /** The database operation. */
    private DatabaseOperation databaseOperation;

    /**
     * Instantiates a new dB operation.
     * 
     * @param databaseOperation
     *            the database operation
     */
    private DBOperation(final DatabaseOperation databaseOperation) {
        this.databaseOperation = DatabaseOperation
                .CLOSE_CONNECTION(DatabaseOperation
                        .TRANSACTION(databaseOperation));
    }

    /**
     * Gets the dbunit operation.
     * 
     * @return the dbunit operation
     */
    public DatabaseOperation getDbunitOperation() {
        return databaseOperation;
    }
}
