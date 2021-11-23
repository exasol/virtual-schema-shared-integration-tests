package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

import java.sql.SQLException;

/**
 * Interface for a virtual schema test setup.
 */
public interface VirtualSchemaTestSetup extends AutoCloseable {

    /**
     * Get the fully qualified name of the virtual schema table.
     * 
     * @return fully qualified name
     */
    public String getFullyQualifiedName();

    /**
     * Deletes all resources of the test setup.
     */
    @Override
    void close() throws SQLException;
}
