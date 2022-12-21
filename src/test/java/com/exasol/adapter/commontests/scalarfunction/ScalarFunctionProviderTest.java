package com.exasol.adapter.commontests.scalarfunction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScalarFunctionProviderTest {
    @Test
    void testSqlExceptionTranslated(@Mock Connection connection) throws SQLException {
        final ScalarFunctionProvider provider = new ScalarFunctionProvider();
        when(connection.createStatement()).thenThrow(new SQLException("Dummy SQLException"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> provider.getScalarFunctions(connection));
        assertThat(exception.getMessage(),
                containsString("Failed to fetch list of scalar functions from the exasol database"));
    }
}