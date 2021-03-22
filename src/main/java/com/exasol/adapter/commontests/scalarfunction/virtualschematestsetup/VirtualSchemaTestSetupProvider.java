package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

public interface VirtualSchemaTestSetupProvider {

    public VirtualSchemaTestSetup createSingleTableVirtualSchemaTestSetup(CreateVirtualSchemaTestSetupRequest request);
}
