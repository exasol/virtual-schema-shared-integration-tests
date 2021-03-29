package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.VirtualSchemaTestSetup;
import com.exasol.errorreporting.ExaError;

/**
 * Request for a table in a {@link VirtualSchemaTestSetup}.
 */
public class TableRequest {
    private final String name;
    private final List<Column> columns;
    private final List<List<Object>> rows;

    private TableRequest(final Builder builder) {
        this.name = builder.name;
        this.columns = builder.columns;
        this.rows = builder.rows;
    }

    public static Builder builder(final String name) {
        return new Builder(name);
    }

    /**
     * Get the name that the virtual schema table should have.
     *
     * @return table name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the columns of the table.
     *
     * @return list of columns
     */
    public List<Column> getColumns() {
        return this.columns;
    }

    /**
     * Get the rows of the tables.
     *
     * @return list of rows
     */
    public List<List<Object>> getRows() {
        return this.rows;
    }

    /**
     * Builder for {@link TableRequest}.
     */
    public static class Builder {
        private final String name;
        private final List<Column> columns;
        private final List<List<Object>> rows;

        private Builder(final String name) {
            this.name = name;
            this.columns = new ArrayList<>();
            this.rows = new ArrayList<>();
        }

        /**
         * Add a column.
         *
         * @param name name of the column
         * @param type type of the column
         * @return self for fluent programming
         */
        public Builder column(final String name, final String type) {
            this.columns.add(new Column(name, type));
            return this;
        }

        /**
         * Add a list of column definitions.
         *
         * @param columns column definitions
         * @return self for fluent programming
         */
        public Builder columns(final List<Column> columns) {
            this.columns.addAll(columns);
            return this;
        }

        /**
         * Add a row.
         *
         * @param rowValues list of row values
         * @return self for fluent programming
         */
        public Builder row(final List<Object> rowValues) {
            this.rows.add(rowValues);
            return this;
        }

        /**
         * Build the {@link TableRequest}.
         *
         * @return built request
         */
        public TableRequest build() {
            validateRowsColumnCount();
            return new TableRequest(this);
        }

        private void validateRowsColumnCount() {
            for (final List<Object> row : this.rows) {
                if (row.size() != this.columns.size()) {
                    throw new IllegalArgumentException(ExaError.messageBuilder("F-VS-SIT-9")
                            .message("The given row has a different number of columns than the table.").toString());
                }
            }
        }
    }
}
