package com.exasol.adapter.commontests.scalarfunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.*;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.Column;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.TableRequest;
import com.exasol.adapter.metadata.DataType;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;

/**
 * This class is a test for {@link ScalarFunctionsTestBase}. It implements a Virtual Schema dialect that does not use a
 * virtual schema but directly returns the Exasol table.
 */
@Testcontainers
public class ScalarFunctionsTestBaseIT extends ScalarFunctionsTestBase
        implements TestSetup, VirtualSchemaTestSetupProvider {
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> CONTAINER = new ExasolContainer<>()
            .withReuse(true);
    private static Connection connection;
    private static ExasolObjectFactory exasolObjectFactory;

    @BeforeAll
    static void beforeAll() throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        connection = CONTAINER.createConnection();
        exasolObjectFactory = new ExasolObjectFactory(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        connection.close();
    }

    @Override
    protected TestSetup getTestSetup() {
        return this;
    }

    @Override
    public Set<String> getDialectSpecificExcludes() {
        return Collections.emptySet();
    }

    @Override
    public VirtualSchemaTestSetupProvider getVirtualSchemaTestSetupProvider() {
        return this;
    }

    @Override
    public Connection createExasolConnection() throws SQLException {
        return CONTAINER.createConnection();
    }

    @Override
    public String getDataTypeThatThisAdapterMapsTo(final DataType type) {
        return type.toString();
    }

    @Override
    public VirtualSchemaTestSetup createSingleTableVirtualSchemaTestSetup(
            final CreateVirtualSchemaTestSetupRequest request) {
        final ExasolSchema schema = exasolObjectFactory.createSchema(getUniqueIdentifier());
        for (final TableRequest tableRequest : request.getTableRequests()) {
            final Table table = buildTable(schema, tableRequest);
            for (final List<Object> row : tableRequest.getRows()) {
                table.insert(row.toArray());
            }
        }
        return new TestDbBuilderVirtualSchemaTestSetup(schema);
    }

    private Table buildTable(final ExasolSchema schema, final TableRequest tableRequest) {
        final Table.Builder tableBuilder = schema.createTableBuilder(tableRequest.getName());
        for (final Column column : tableRequest.getColumns()) {
            tableBuilder.column(column.getName(), column.getType());
        }
        return tableBuilder.build();
    }

    private static class TestDbBuilderVirtualSchemaTestSetup implements VirtualSchemaTestSetup {
        private final Schema schema;

        private TestDbBuilderVirtualSchemaTestSetup(final Schema schema) {
            this.schema = schema;
        }

        @Override
        public String getFullyQualifiedName() {
            return this.schema.getFullyQualifiedName();
        }

        @Override
        public void close() throws SQLException {
            this.schema.drop();
        }
    }
}
