package com.exasol.adapter.commontests.scalarfunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VirtualSchemaRunVerifierTest {
    private static final String TABLE_NAME = "TEST_TABLE";
    private Connection connection;
    private Statement statement;
    private VirtualSchemaRunVerifier virtualSchemaRunVerifier;

    @BeforeEach
    void beforeEach() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:derby:memory:test;create=true");
        this.statement = this.connection.createStatement();
        this.virtualSchemaRunVerifier = this.createVirtualSchemaRunVerifier();
        this.createTestTable();
    }

    private VirtualSchemaRunVerifier createVirtualSchemaRunVerifier() {
        final ScalarFunctionQueryBuilder virtualSchemaQueryBuilder = new ScalarFunctionQueryBuilder(TABLE_NAME);
        return new VirtualSchemaRunVerifier(virtualSchemaQueryBuilder);
    }

    private void createTestTable() throws SQLException {
        this.statement.execute("CREATE TABLE " + TABLE_NAME + " (COL1 INTEGER)");
    }

    @AfterEach
    void afterEach() throws SQLException {
        this.dropTestTable();
    }

    private void dropTestTable() throws SQLException {
        this.statement.execute("DROP TABLE " + TABLE_NAME);
    }

    static Stream<Arguments> quickCheckIfFunctionBehavesSameOnVstestCases() {
        return Stream.of(//
                Arguments.of("testWithEmptyRuns", "COUNT", Collections.emptyList(), true), //
                Arguments.of("testWithSingleSuccesfulRun", "COUNT", getSingleRunListWith("*", 0), true), //
                Arguments.of("testWithSingleFailingRun", "COUNT", getSingleRunListWith("*", 1), false), //
                Arguments.of("testWithMultipleSuccesfulRuns", "COUNT", getMultipleRunsWith("*", 0), true), //
                Arguments.of("testWithMultipleFailingRuns", "COUNT", getMultipleRunsWith("*", 1), false), //
                Arguments.of("testWithMultipleRunsOneFailingAndOneSuccessful", "COUNT",
                        List.of(getRunWith("*", 0), getRunWith("*", 1)), false), //
                Arguments.of("testWithFailingFunction", "FAILING_FUNCTION", getSingleRunListWith("*", 0), false));
    }

    private static List<ScalarFunctionLocalRun> getMultipleRunsWith(final String parameters, final int result) {
        return List.of(new ScalarFunctionLocalRun(parameters, result), new ScalarFunctionLocalRun(parameters, result));
    }

    private static List<ScalarFunctionLocalRun> getSingleRunListWith(final String parameters, final Object result) {
        return List.of(getRunWith(parameters, result));
    }

    private static ScalarFunctionLocalRun getRunWith(final String parameters, final Object result) {
        return new ScalarFunctionLocalRun(parameters, result);
    }

    @ParameterizedTest
    @MethodSource("quickCheckIfFunctionBehavesSameOnVstestCases")
    void testQuickCheckIfFunctionBehavesSameOnVs(final String testCase, final String function,
            final List<ScalarFunctionLocalRun> runs, final boolean result) {
        final boolean value = this.virtualSchemaRunVerifier.quickCheckIfFunctionBehavesSameOnVs(function, runs,
                this.statement);
        assertThat(testCase, value, equalTo(result));
    }
}