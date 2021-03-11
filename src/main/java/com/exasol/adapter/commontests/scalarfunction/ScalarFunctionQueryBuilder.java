package com.exasol.adapter.commontests.scalarfunction;

/**
 * This class builds SQL queries with a scalar function call.
 */
public class ScalarFunctionQueryBuilder {
    private final String tableName;

    /**
     * Create a new instance of {@link ScalarFunctionQueryBuilder}.
     * 
     * @param tableName name of the table to query
     */
    public ScalarFunctionQueryBuilder(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * Build a SQL query with a scalar function call.
     * 
     * @param functionCall scalar function call
     * @return built query
     */
    public String buildQueryFor(final String functionCall) {
        return "SELECT " + functionCall + " FROM " + this.tableName;
    }
}
