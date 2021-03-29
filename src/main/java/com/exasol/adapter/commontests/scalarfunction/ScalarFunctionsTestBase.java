package com.exasol.adapter.commontests.scalarfunction;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.CreateVirtualSchemaTestSetupRequest;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.VirtualSchemaTestSetup;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.Column;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.TableRequest;
import com.exasol.adapter.metadata.DataType;
import com.exasol.errorreporting.ExaError;
import com.exasol.matcher.TypeMatchMode;

/**
 * This is an abstract smoke test for all scalar functions, except the geospacial functions (ST_*).
 * <p>
 * These tests can be executed in parallel. In order to do so, add the system property
 * {@code -Djunit.jupiter.execution.parallel.enabled=true} to your junit JVM.
 * </p>
 */
@SuppressWarnings("java:S5786") // class is public since it is an abstract test
@Execution(value = ExecutionMode.CONCURRENT)
public abstract class ScalarFunctionsTestBase {
    private static final String MY_COLUMN = "MY_COLUMN";
    private static final String MY_TABLE = "MY_TABLE";
    /**
     * These functions are tested separately in {@link testFunctionsWithNoParenthesis(String)} )}
     */
    private static final Set<String> FUNCTIONS_WITH_NO_PARENTHESIS = Set.of("localtimestamp", "sysdate",
            "current_schema", "current_statement", "current_session", "current_date", "current_user",
            "current_timestamp");
    /**
     * These scalar functions have a special syntax, so we define explicit test for them below.
     */
    private static final Set<String> EXCLUDED_SCALAR_FUNCTIONS = Set.of("case", "cast", "float_div",
            "session_parameter", "rand", "random", "add", "sub", "mult", "neg", "sys_guid", "systimestamp",
            "convert_tz", "date_trunc", "numtodsinterval", "numtoyminterval", "to_yminterval", "to_dsinterval",
            "json_value", "extract", "posix_time", "greatest", "now", "position");
    private static final String LOCAL_COPY_TABLE_NAME = "LOCAL_COPY";
    private static final String LOCAL_COPY_SCHEMA = "EXASOL";
    private static final String LOCAL_COPY_FULL_TABLE_NAME = LOCAL_COPY_SCHEMA + "." + LOCAL_COPY_TABLE_NAME;

    /**
     * For sum functions we just can guess the parameters because they only work for very specific input. For those we
     * define explicit parameters here. You can define multiple parameter combinations here (by adding multiple list
     * entries). By that you can test a function with different values.
     */
    private static final Logger LOGGER = Logger.getLogger(ScalarFunctionsTestBase.class.getName());

    private static int idCounter = 0;

    /**
     * Get an unique identifier for test resources.
     * 
     * @return unique string
     */
    protected static String getUniqueIdentifier() {
        idCounter++;
        return "id" + idCounter;
    }

    static Stream<Arguments> getScalarFunctionWithNoParameters() {
        return FUNCTIONS_WITH_NO_PARENTHESIS.stream().map(Arguments::of);
    }

    /**
     * Get an test setup for testing this dialect.
     * <p>
     * The abstract base will call this method multiple times. Please return the same instance (typically this) for all
     * invocation.
     * </p>
     * 
     * @return test setup
     */
    protected abstract TestSetup getTestSetup();

    private void runOnExasol(final ExasolExecutable exasolExecutable) {
        try (final Connection connection = getTestSetup().createExasolConnection();
                final Statement statement = connection.createStatement()) {
            exasolExecutable.runOnExasol(statement);
        } catch (final SQLException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VS-SIT-1").message("Failed to execute command on Exasol.").toString(),
                    exception);
        }
    }

    @FunctionalInterface
    private interface ExasolExecutable {
        void runOnExasol(Statement statement) throws SQLException;
    }

    /**
     * This is a workaround to disable tests. The proper method would be to add these tests to the failsafe plugins
     * excludes. This is however not possible due to a bug: https://issues.apache.org/jira/browse/SUREFIRE-1880
     *
     * @param function function to test
     * @return {@code true} if test is disabled by dialect
     */
    private boolean isTestDisabledFor(final String function) {
        final Set<String> dialectSpecificExcludes = getTestSetup().getDialectSpecificExcludes();
        if (dialectSpecificExcludes.contains(function.toLowerCase())) {
            LOGGER.log(Level.FINE, "Skipping test for {0} since it was disabled for this dialect.", function);
            return true;
        }
        return false;
    }

    private VirtualSchemaTestSetup buildVirtualSchemaTableWithColumnOfExasolType(final DataType exasolType,
            final Object valueForSingleRow) {
        final String booleanType = getTestSetup().getDataTypeThatThisAdapterMapsTo(exasolType);
        final CreateVirtualSchemaTestSetupRequest request = new CreateVirtualSchemaTestSetupRequest(
                TableRequest.builder(MY_TABLE).column(MY_COLUMN, booleanType).row(List.of(valueForSingleRow)).build());
        return getTestSetup().getVirtualSchemaTestSetupProvider().createSingleTableVirtualSchemaTestSetup(request);
    }

    /**
     * This test case is for functions that do not have parenthesis (e.g. CURRENT_SCHEMA).
     * <p>
     * Since the result of all of this functions is different on different databases or at different time, we just test
     * that they don't throw an exception on the Virtual Schema.
     * </p>
     *
     * @param function function to test.
     */
    @ParameterizedTest
    @MethodSource("getScalarFunctionWithNoParameters")
    void testFunctionsWithNoParenthesis(final String function) throws SQLException {
        if (isTestDisabledFor(function)) {
            return;
        }
        try (final VirtualSchemaTestSetup virtualSchema = getBooleanVirtualSchema(true)) {
            assertScalarFunctionQuery(virtualSchema, function, table().row(anyOf(instanceOf(Object.class), nullValue()))
                    .matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testSystimestamp() throws SQLException {
        if (isTestDisabledFor("SYSTIMESTAMP")) {
            return;
        }
        setSessionTimezone("UTC");
        final Timestamp actualTimestamp = getActualSystimestamp();
        final Timestamp lowerBorder = new Timestamp(actualTimestamp.getTime() - 1000 * 20);
        final Timestamp upperBorder = new Timestamp(actualTimestamp.getTime() + 1000 * 20);
        try (final VirtualSchemaTestSetup virtualSchema = getBooleanVirtualSchema(true)) {
            assertScalarFunctionQuery(virtualSchema, "SYSTIMESTAMP",
                    table().row(allOf(greaterThan(lowerBorder), lessThan(upperBorder)))
                            .matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    private Timestamp getActualSystimestamp() throws SQLException {
        try (final Connection connection = getTestSetup().createExasolConnection();
                final Statement statement = connection.createStatement();
                final ResultSet actualResult = statement.executeQuery("SELECT SYSTIMESTAMP FROM DUAL;")) {
            actualResult.next();
            return actualResult.getTimestamp(1);
        }
    }

    /**
     * For the random function it obviously makes no sense to check if the output is the same on two runs. For that
     * reason we check here if it produces at least some different results.
     */
    @Test
    void testRand() throws SQLException {
        if (isTestDisabledFor("RAND"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getBooleanVirtualSchema(true)) {
            assertScalarFunctionQuery(virtualSchema, "RAND()",
                    table().row(lessThanOrEqualTo(1.0)).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testSysGUID() throws SQLException {
        if (isTestDisabledFor("SYS_GUID"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getBooleanVirtualSchema(true)) {
            assertScalarFunctionQuery(virtualSchema, "SYS_GUID()",
                    table().row(instanceOf(Object.class)).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    void assertScalarFunctionQuery(final VirtualSchemaTestSetup virtualSchema, final String query,
            final Matcher<ResultSet> resultSetMatcher) {
        runOnExasol(statement -> assertScalarFunctionQuery(virtualSchema, query, resultSetMatcher, statement));
    }

    private void assertScalarFunctionQuery(final VirtualSchemaTestSetup virtualSchema, final String query,
            final Matcher<ResultSet> resultSetMatcher, final Statement statement) throws SQLException {
        final String sql = "SELECT " + query + " FROM " + virtualSchema.getFullyQualifiedName() + ".\"" + MY_TABLE
                + "\"";
        try (final ResultSet virtualSchemaTableResult = statement.executeQuery(sql)) {
            assertThat(virtualSchemaTableResult, resultSetMatcher);
        }
    }

    private VirtualSchemaTestSetup getIntegerVirtualSchema(final int valueForSingleRow) {
        return buildVirtualSchemaTableWithColumnOfExasolType(DataType.createDecimal(18, 0), valueForSingleRow);
    }

    @ParameterizedTest
    @CsvSource({ "ADD, +, 4", "SUB, -, 0", "MULT, *, 4", "FLOAT_DIV, /, 1" })
    void testSimpleArithmeticFunctions(final String function, final String operator, final int expectedResult)
            throws SQLException {
        if (isTestDisabledFor(function)) {
            return;
        }
        try (final VirtualSchemaTestSetup virtualSchema = getIntegerVirtualSchema(2)) {
            final String scalarFunctionCall = MY_COLUMN + " " + operator + " " + MY_COLUMN;
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expectedResult).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testCast() throws SQLException {
        if (isTestDisabledFor("CAST"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getIntegerVirtualSchema(2)) {
            final String scalarFunctionCall = "CAST(" + MY_COLUMN + " AS VARCHAR(254) UTF8)";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(String.valueOf(2)).matches(TypeMatchMode.STRICT));
        }
    }

    @ParameterizedTest
    @CsvSource({ "-2, 0", "2, 2" })
    void testGreatest(final int input, final int expectedOutput) throws SQLException {
        if (isTestDisabledFor("GREATEST"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getIntegerVirtualSchema(input)) {
            final String scalarFunctionCall = "GREATEST(" + MY_COLUMN + ", 0)";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expectedOutput).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    private VirtualSchemaTestSetup getDoubleVirtualSchema(final double valueForSingleRow) {
        return buildVirtualSchemaTableWithColumnOfExasolType(DataType.createDouble(), valueForSingleRow);
    }

    @ParameterizedTest
    @CsvSource({ "0.1, 0", "0.5, 1", "0.9, 1" })
    void testRound(final double input, final double expectedOutput) throws SQLException {
        if (isTestDisabledFor("ROUND"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getDoubleVirtualSchema(input)) {
            final String scalarFunctionCall = "ROUND(" + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expectedOutput).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    private VirtualSchemaTestSetup getBooleanVirtualSchema(final boolean valueForSingleRow) {
        return buildVirtualSchemaTableWithColumnOfExasolType(DataType.createBool(), valueForSingleRow);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testNeg(final boolean input) throws SQLException {
        if (isTestDisabledFor("NEG")) {
            return;
        }
        try (final VirtualSchemaTestSetup virtualSchema = getBooleanVirtualSchema(input)) {
            final String scalarFunctionCall = "NOT " + MY_COLUMN;
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall, table().row(!input).matches());
        }
    }

    private VirtualSchemaTestSetup getStringVirtualSchema(final String valueForSingleRow) {
        return buildVirtualSchemaTableWithColumnOfExasolType(DataType.createVarChar(254, DataType.ExaCharset.UTF8),
                valueForSingleRow);
    }

    /**
     * Test the SQL CASE statement.
     *
     * @implNote This test does some unnecessary math on a column of the virtual schema to make sure that this case
     *           statement is sent to the virtual schema if possible and not evaluated before. If, however, the virtual
     *           schema does not have the FLOAT_DIV or ADD capability this does not work.
     *
     * @param input          input value
     * @param expectedResult expected output
     */
    @ParameterizedTest
    @CsvSource({ //
            "a, 1", //
            "b, 2", //
            "c, 3" //
    })
    void testCase(final String input, final int expectedResult) throws SQLException {
        if (isTestDisabledFor("CASE"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema(input)) {
            final String scalarFunctionCall = "CASE " + MY_COLUMN + " WHEN 'a' THEN 1 WHEN 'b' THEN 2 ELSE 3 END";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expectedResult).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testConvertTz() throws SQLException, ParseException {
        if (isTestDisabledFor("CONVERT_TZ"))
            return;
        final Timestamp expected = new Timestamp(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-03-25 04:30:00").getTime());
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("UTC")) {
            final String scalarFunctionCall = "CONVERT_TZ(TIMESTAMP '2012-03-25 02:30:00' , " + MY_COLUMN
                    + ", 'Europe/Berlin')";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expected).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testDateTrunc() throws SQLException, ParseException {
        if (isTestDisabledFor("DATE_TRUNC"))
            return;
        final Timestamp expectedResult = new Timestamp(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2006-12-01 00:00:00.0").getTime());
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("month")) {
            final String scalarFunctionCall = "DATE_TRUNC(" + MY_COLUMN + ", TIMESTAMP '2006-12-31 23:59:59')";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(expectedResult).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testNumToDsInterval() throws SQLException {
        if (isTestDisabledFor("NUMTODSINTERVAL"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("HOUR")) {
            final String scalarFunctionCall = "NUMTODSINTERVAL(3.2, " + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row("+000000000 03:12:00.000000000").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testNumToYmInterval() throws SQLException {
        if (isTestDisabledFor("NUMTOYMINTERVAL"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("YEAR")) {
            final String scalarFunctionCall = "NUMTOYMINTERVAL(3.5, " + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row("+000000003-06").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testToYmInterval() throws SQLException {
        if (isTestDisabledFor("TO_YMINTERVAL"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("3-11")) {
            final String scalarFunctionCall = "TO_YMINTERVAL(" + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row("+000000003-11").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testToDsInterval() throws SQLException {
        if (isTestDisabledFor("TO_DSINTERVAL"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("3 10:59:59.123")) {
            final String scalarFunctionCall = "TO_DSINTERVAL(" + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row("+000000003 10:59:59.123000000").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testJsonValue() throws SQLException {
        if (isTestDisabledFor("JSON_VALUE"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("{\"name\" : \"Test\"}")) {
            final String scalarFunctionCall = "JSON_VALUE(" + MY_COLUMN + ", '$.name')";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row("Test").matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @Test
    void testPosition() throws SQLException {
        if (isTestDisabledFor("position"))
            return;
        try (final VirtualSchemaTestSetup virtualSchema = getStringVirtualSchema("my long string")) {
            final String scalarFunctionCall = "POSITION('long' IN " + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(4).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    private VirtualSchemaTestSetup getTimestampVirtualSchema(final Timestamp valueForSingleRow) {
        return buildVirtualSchemaTableWithColumnOfExasolType(DataType.createTimestamp(false), valueForSingleRow);
    }

    @Test
    void testExtract() throws SQLException {
        if (isTestDisabledFor("EXTRACT"))
            return;
        setSessionTimezone("UTC");
        try (final VirtualSchemaTestSetup virtualSchema = getTimestampVirtualSchema(new Timestamp(1000))) {
            final String scalarFunctionCall = "EXTRACT(MONTH FROM " + MY_COLUMN + ")";
            assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                    table().row(1).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
        }
    }

    @ParameterizedTest
    @CsvSource({ //
            "UTC, 1", //
            "Europe/Berlin, -3599",//
    })
    void testPosixTime(final String timeZone, final long expectedResult) {
        if (isTestDisabledFor("POSIX_TIME"))
            return;
        runOnExasol(statement -> {
            statement.executeUpdate("ALTER SESSION SET TIME_ZONE='" + timeZone + "';");
            try (final VirtualSchemaTestSetup virtualSchema = getTimestampVirtualSchema(new Timestamp(1000))) {
                final String scalarFunctionCall = "POSIX_TIME(" + MY_COLUMN + ")";
                assertScalarFunctionQuery(virtualSchema, scalarFunctionCall,
                        table().row(expectedResult).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK), statement);
            }
        });
    }

    private void setSessionTimezone(final String timeZone) {
        runOnExasol(statement -> statement.executeUpdate("ALTER SESSION SET TIME_ZONE='" + timeZone + "';"));
    }

    /**
     * This test automatically finds parameter combinations by permuting a set of different values. Then it verifies
     * that on of the parameter combinations that did not cause an exception in on a regular Exasol table, succeeds on
     * the virtual schema tale. In addition this test asserts that all queries that succeed on the virtual and the
     * regular table have the same result.
     */
    @Nested
    @TestInstance(PER_CLASS)
    @Execution(value = ExecutionMode.CONCURRENT)
    @Tag("WithAutomaticParameterDiscovery")
    class WithAutomaticParameterDiscovery {
        private List<String> columnsWithType;
        private ScalarFunctionsParameterCache parameterCache;
        private ScalarFunctionParameterFinder parameterFinder;
        private VirtualSchemaRunVerifier virtualSchemaRunVerifier;
        private final List<DataTypeWithExampleValue> dataTypeWithExampleValues = List.of(
                new DataTypeWithExampleValue(DataType.createDouble(), 0.5),
                new DataTypeWithExampleValue(DataType.createDecimal(18, 0), 2),
                new DataTypeWithExampleValue(DataType.createBool(), true),
                new DataTypeWithExampleValue(DataType.createVarChar(2, DataType.ExaCharset.UTF8), "a"),
                new DataTypeWithExampleValue(DataType.createDate(), new Date(1000)),
                new DataTypeWithExampleValue(DataType.createTimestamp(false), new Timestamp(1001)));
        private VirtualSchemaTestSetup virtualSchemaTestSetup;

        @BeforeAll
        void beforeAll() {
            this.virtualSchemaTestSetup = createVirtualSchemaTestSetup();
            runOnExasol(statement -> {
                statement.executeUpdate("CREATE SCHEMA " + LOCAL_COPY_SCHEMA);
                final String testTable = this.virtualSchemaTestSetup.getFullyQualifiedName() + ".\"" + MY_TABLE + "\"";
                statement
                        .executeUpdate("CREATE TABLE " + LOCAL_COPY_FULL_TABLE_NAME + " as SELECT * FROM " + testTable);
                this.columnsWithType = getColumnsOfTable(LOCAL_COPY_FULL_TABLE_NAME);
                this.parameterCache = new ScalarFunctionsParameterCache();
                this.parameterFinder = new ScalarFunctionParameterFinder(this.columnsWithType,
                        new ScalarFunctionQueryBuilder(LOCAL_COPY_FULL_TABLE_NAME), this.parameterCache);
                this.virtualSchemaRunVerifier = new VirtualSchemaRunVerifier(new ScalarFunctionQueryBuilder(testTable));
            });
        }

        private List<String> getColumnsOfTable(final String tableName) {
            final List<String> names = new ArrayList<>();
            runOnExasol(statement -> {
                try (final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final int columnCount = metaData.getColumnCount();
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        names.add("\"" + metaData.getColumnName(columnIndex) + "\"");
                    }
                }
            });
            return names;
        }

        private VirtualSchemaTestSetup createVirtualSchemaTestSetup() {
            final List<Column> columns = this.dataTypeWithExampleValues.stream()
                    .map(dataTypeWithExampleValue -> new Column(dataTypeWithExampleValue.getExasolDataType().toString(),
                            getTestSetup()
                                    .getDataTypeThatThisAdapterMapsTo(dataTypeWithExampleValue.getExasolDataType())))
                    .collect(Collectors.toList());
            final List<Object> rowWithExampleValues = this.dataTypeWithExampleValues.stream()
                    .map(DataTypeWithExampleValue::getExampleValue).collect(Collectors.toList());
            final CreateVirtualSchemaTestSetupRequest request = new CreateVirtualSchemaTestSetupRequest(
                    TableRequest.builder(MY_TABLE).columns(columns).row(rowWithExampleValues).build());
            return getTestSetup().getVirtualSchemaTestSetupProvider().createSingleTableVirtualSchemaTestSetup(request);
        }

        @AfterAll
        void afterAll() throws SQLException {
            this.virtualSchemaTestSetup.close();
        }

        /**
         * Test for most of the scala functions. Since they are so many, it's too much effort to write all parameter
         * combinations here. Instead this test tries all permutations on an Exasol table and in case they do not cause
         * an exception asserts that they produce the same result on the virtual schema table.
         *
         * @param function function to test
         */
        @ParameterizedTest
        @MethodSource("getScalarFunctions")
        void testScalarFunctions(final String function) {
            runOnExasol(statement -> {
                final List<ScalarFunctionLocalRun> successfulScalarFunctionLocalRuns = this.parameterFinder
                        .findOrGetFittingParameters(function, statement);
                if (successfulScalarFunctionLocalRuns.isEmpty()) {
                    throw new IllegalStateException(ExaError.messageBuilder("E-VS-SIT-2")
                            .message("Non of the parameter combinations lead to a successful run.").toString());
                } else {
                    this.parameterCache.removeFunction(function);
                    if (!this.virtualSchemaRunVerifier.quickCheckIfFunctionBehavesSameOnVs(function,
                            successfulScalarFunctionLocalRuns, statement)) {
                        LOGGER.log(Level.FINE, "Quick test failed for {0}. Running full checks.", function);
                        final List<String> successfulParameters = this.virtualSchemaRunVerifier
                                .assertFunctionBehavesSameOnVirtualSchema(function, successfulScalarFunctionLocalRuns,
                                        statement);
                        this.parameterCache.setFunctionsValidParameterCombinations(function, successfulParameters);
                    } else {
                        this.parameterCache.setFunctionsValidParameterCombinations(function,
                                successfulScalarFunctionLocalRuns.stream().map(ScalarFunctionLocalRun::getParameters)
                                        .collect(Collectors.toList()));
                    }
                    this.parameterCache.flush();
                }
            });
        }

        Stream<Arguments> getScalarFunctions() {
            try (final Connection exasolConnection = getTestSetup().createExasolConnection()) {
                return new ScalarFunctionProvider().getScalarFunctions(exasolConnection).stream()//
                        .filter(function -> !EXCLUDED_SCALAR_FUNCTIONS.contains(function)
                                && !isTestDisabledFor(function) && !FUNCTIONS_WITH_NO_PARENTHESIS.contains(function))//
                        .map(Arguments::of);
            } catch (final SQLException exception) {
                throw new IllegalStateException(ExaError.messageBuilder("E-VS-SIT-8")
                        .message("Failed to get supported scalar functions.").toString(), exception);
            }
        }
    }
}
