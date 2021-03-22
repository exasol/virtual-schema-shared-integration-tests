package com.exasol.adapter.commontests.scalarfunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.VirtualSchemaTestSetupProvider;
import com.exasol.adapter.metadata.DataType;

/**
 * Interface for a dialect specific test setup.
 */
public interface TestSetup {

    /**
     * Returns a set of scalar functions that should not be tested for this dialect.
     *
     * @return set of functions
     */
    public Set<String> getDialectSpecificExcludes();

    /**
     * Get a dialect specific test setup provider.
     *
     * @return dialect specific {@link VirtualSchemaTestSetupProvider}
     */
    public VirtualSchemaTestSetupProvider getVirtualSchemaTestSetupProvider();

    /**
     * Get a connection to the Exasol database.
     *
     * @return connection to the Exasol database
     * @throws SQLException if creating the connection failed
     */
    public Connection createExasolConnection() throws SQLException;

    /**
     * Get a data type of the dialects data source that the adapter will map to the given Exasol data type.
     * <p>
     * Please always return the most equal type. For example for {@code VARCHAR(254) UTF8} please also return some text
     * type and not a GEOMETRY (You could come to this idea because it's also mapped to a {@code VARCHAR(254) UTF8}).
     * </p>
     *
     * @param type exasol data type
     * @return data type of the dialects data source
     */
    public String getDataTypeThatThisAdapterMapsTo(final DataType type);
}
