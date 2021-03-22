package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

import java.sql.SQLException;

public interface VirtualSchemaTestSetup extends AutoCloseable {

    /**
     * Get the fully qualified name of the virtual schema table.
     * 
     * @return fully qualified name
     */
    public String getFullyQualifiedName();

    @Override
    void close() throws SQLException;
}
