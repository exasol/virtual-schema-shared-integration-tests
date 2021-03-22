package com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup;

import java.util.Arrays;
import java.util.List;

import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.TableRequest;

/**
 * This class describes how a {@link VirtualSchemaTestSetup} should look like.
 * <p>
 * We could can not use the test-database-builder-java class structures here, since they define the content of tables
 * after they are created. This class structure needs to express the whole test setup. Otherwise it would not be
 * possible to check if a certain test setup was already created.
 * </p>
 */
public class CreateVirtualSchemaTestSetupRequest {
    private final List<TableRequest> tableRequests;

    /**
     * Create a new instance of {@link VirtualSchemaTestSetup}.
     *
     * @param tableRequests description how the tables should be created
     */
    public CreateVirtualSchemaTestSetupRequest(final List<TableRequest> tableRequests) {
        this.tableRequests = tableRequests;
    }

    /**
     * Create a new instance of {@link VirtualSchemaTestSetup}.
     * 
     * @param tableRequests description how the tables should be created
     */
    public CreateVirtualSchemaTestSetupRequest(final TableRequest... tableRequests) {
        this(Arrays.asList(tableRequests));
    }

    /**
     * Get the tables of this request.
     * 
     * @return list of {@link TableRequest}.
     */
    public List<TableRequest> getTableRequests() {
        return this.tableRequests;
    }
}
