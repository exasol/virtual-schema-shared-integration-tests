package com.exasol.adapter.commontests.scalarfunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.containers.ExasolContainer;

@Testcontainers
class ScalarFunctionProviderTest {
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> CONTAINER = new ExasolContainer<>()
            .withReuse(true);

    @Test
    void test() throws SQLException {
        try (final Connection connection = CONTAINER.createConnection()) {
            final Set<String> scalarFunctions = new ScalarFunctionProvider().getScalarFunctions(connection);
            assertThat(scalarFunctions, hasItem("abs"));
        }
    }
}