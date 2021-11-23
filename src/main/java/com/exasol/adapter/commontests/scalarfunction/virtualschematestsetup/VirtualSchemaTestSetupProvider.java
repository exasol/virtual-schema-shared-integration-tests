package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

/**
 * A provider for {@link VirtualSchemaTestSetup}.
 */
public interface VirtualSchemaTestSetupProvider {

    /**
     * Create a new {@link VirtualSchemaTestSetup} containing the given table.
     * 
     * @param request the specification for the table that is required for the test.
     * @return the new {@link VirtualSchemaTestSetup}
     */
    public VirtualSchemaTestSetup createSingleTableVirtualSchemaTestSetup(CreateVirtualSchemaTestSetupRequest request);
}
