package com.exasol.adapter.commontests.scalarfunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.junit.jupiter.api.*;

import com.exasol.containers.ExasolContainer;

class ScalarFunctionProviderIT {
    @SuppressWarnings("resource") // Will be closed by stopContainer()
    private static final ExasolContainer<? extends ExasolContainer<?>> CONTAINER = new ExasolContainer<>()
            .withReuse(true);

    @BeforeAll
    static void startContainer() {
        CONTAINER.start();
    }

    @AfterAll
    static void stopContainer() {
        CONTAINER.stop();
    }

    @Test
    void test() throws SQLException {
        try (final Connection connection = CONTAINER.createConnection()) {
            final Set<String> scalarFunctions = new ScalarFunctionProvider().getScalarFunctions(connection);
            assertThat(scalarFunctions, hasItem("abs"));
        }
    }
}
