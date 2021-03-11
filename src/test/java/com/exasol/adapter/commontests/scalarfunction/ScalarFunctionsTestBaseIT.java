package com.exasol.adapter.commontests.scalarfunction;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;

/**
 * This class is a test for {@link ScalarFunctionsTestBase}. It implements a Virtual Schema dialect that does not use a
 * virtual schema but directly returns the Exasol table.
 */
@Testcontainers
public class ScalarFunctionsTestBaseIT extends ScalarFunctionsTestBase {
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> CONTAINER = new ExasolContainer<>()
            .withReuse(true);
    private static Connection connection;
    private static ExasolObjectFactory exasolObjectFactory;

    @BeforeAll
    static void beforeAll() throws SQLException {
        connection = CONTAINER.createConnection();
        exasolObjectFactory = new ExasolObjectFactory(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        connection.close();
    }

    @Override
    protected Set<String> getDialectSpecificExcludes() {
        return Collections.emptySet();
    }

    @Override
    protected SingleTableVirtualSchemaTestSetup createVirtualSchemaTableWithExamplesForAllDataTypes()
            throws SQLException {
        return new ExasolNativeSingleTableVirtualSchemaTestSetup() {
            @Override
            protected Table createTable() {
                return this.getSchema().createTableBuilder(getUniqueIdentifier())//
                        .column("floating_point", "DOUBLE")//
                        .column("number", "integer")//
                        .column("boolean", "boolean")//
                        .column("string", "VARCHAR(2) UTF8")//
                        .column("date", "DATE")//
                        .column("timestamp", "TIMESTAMP").build()
                        .insert(0.5, 2, true, "a", new Date(1000), new Timestamp(1001));
            }
        };
    }

    @Override
    protected Connection createExasolConnection() throws SQLException {
        return CONTAINER.createConnection();
    }

    @Override
    protected SingleRowSingleTableVirtualSchemaTestSetup<Timestamp> createDateVirtualSchemaTable() throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return createTestTable("TIMESTAMP");
    }

    @Override
    protected SingleRowSingleTableVirtualSchemaTestSetup<Integer> createIntegerVirtualSchemaTable()
            throws SQLException {
        return createTestTable("INTEGER");
    }

    @Override
    protected SingleRowSingleTableVirtualSchemaTestSetup<Double> createDoubleVirtualSchemaTable() throws SQLException {
        return createTestTable("DOUBLE");
    }

    @Override
    protected SingleRowSingleTableVirtualSchemaTestSetup<Boolean> createBooleanVirtualSchemaTable()
            throws SQLException {
        return createTestTable("BOOLEAN");
    }

    @Override
    protected SingleRowSingleTableVirtualSchemaTestSetup<String> createStringVirtualSchemaTable() throws SQLException {
        return createTestTable("VARCHAR(254) UTF8");
    }

    private <T> SingleRowSingleTableVirtualSchemaTestSetup<T> createTestTable(final String type) {
        return new SingleRowExasolNativeSingleTableVirtualSchemaTestSetup<>() {
            @Override
            protected Table createTable() {
                return this.getSchema().createTableBuilder(getUniqueIdentifier())//
                        .column("my_column", type).build();
            }
        };
    }

    private static abstract class ExasolNativeSingleTableVirtualSchemaTestSetup
            implements SingleTableVirtualSchemaTestSetup {
        private final Table table;
        private final Schema schema;

        public ExasolNativeSingleTableVirtualSchemaTestSetup() {
            this.schema = exasolObjectFactory.createSchema(getUniqueIdentifier());
            this.table = createTable();
        }

        protected abstract Table createTable();

        @Override
        public String getFullyQualifiedName() {
            return this.table.getFullyQualifiedName();
        }

        @Override
        public void drop() {
            this.table.drop();
            this.schema.drop();
        }

        public Schema getSchema() {
            return this.schema;
        }

        public Table getTable() {
            return this.table;
        }
    }

    private static abstract class SingleRowExasolNativeSingleTableVirtualSchemaTestSetup<T> extends
            ExasolNativeSingleTableVirtualSchemaTestSetup implements SingleRowSingleTableVirtualSchemaTestSetup<T> {

        @Override
        public void truncateTable() throws SQLException {
            try (final Statement statement = connection.createStatement()) {
                statement.executeUpdate("TRUNCATE TABLE " + this.getTable().getFullyQualifiedName());
            }
        }

        @Override
        public void insertValue(final T value) {
            getTable().insert(value);
        }
    }
}
