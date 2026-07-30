package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

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
     * Delete all resources of the test setup.
     */
    @Override
    void close();
}
