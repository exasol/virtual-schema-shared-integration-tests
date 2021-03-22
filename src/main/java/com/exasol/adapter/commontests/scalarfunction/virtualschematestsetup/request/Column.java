package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request;

/**
 * Column definition.
 */
public class Column {
    private final String name;
    private final String type;

    /**
     * Create a new instance of {@link Column}.
     * 
     * @param name name of the column
     * @param type data sources data type of the column
     */
    public Column(final String name, final String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Get the name of the column.
     * 
     * @return name of the column
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the type of the column.
     * <p>
     * The type is a data type string for the dialects data source (not an Exasol type).
     * </p>
     * 
     * @return data type string
     */
    public String getType() {
        return this.type;
    }
}
