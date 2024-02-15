package com.exasol.adapter.commontests.scalarfunction;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;

import com.exasol.errorreporting.ExaError;
import com.exasol.matcher.CellMatcherFactory;
import com.exasol.matcher.TypeMatchMode;

/**
 * This class checks if the scalar function produces the same result than it did on a local exasol table.
 */
public class VirtualSchemaRunVerifier {
    private static final ScalarFunctionCallBuilder CALL_BUILDER = new ScalarFunctionCallBuilder();
    private static final int BATCH_SIZE = 500;
    private static final Logger LOGGER = Logger.getLogger(VirtualSchemaRunVerifier.class.getName());
    private final ScalarFunctionQueryBuilder virtualSchemaQueryBuilder;

    /**
     * Create a new {@link VirtualSchemaRunVerifier}.
     *
     * @param virtualSchemaQueryBuilder a {@link ScalarFunctionCallBuilder}.
     */
    public VirtualSchemaRunVerifier(final ScalarFunctionQueryBuilder virtualSchemaQueryBuilder) {
        this.virtualSchemaQueryBuilder = virtualSchemaQueryBuilder;
    }

    /**
     * Assert that the function behaves same on the virtual schema as it died on the Exasol table.
     *
     * @param function     scalar function to test
     * @param runsOnExasol Exasol runs (parameter - result pairs) to compare to
     * @param statement    statement to use.
     * @return List of successful parameter combinations
     */
    public List<String> assertFunctionBehavesSameOnVirtualSchema(final String function,
            final List<ScalarFunctionLocalRun> runsOnExasol, final Statement statement) {
        final List<String> successParameters = new ArrayList<>();
        final List<String> failedQueries = new ArrayList<>();
        boolean hadMismatches = false;
        for (final ScalarFunctionLocalRun scalarFunctionLocalRun : runsOnExasol) {
            if (!assertSingleRunBehavesSameOnVirtualSchema(function, statement, successParameters, failedQueries,
                    scalarFunctionLocalRun)) {
                hadMismatches = true;
            }
        }
        if (hadMismatches) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSSIT-10")
                    .message("Some runs of this function had different output. See above log messages.").toString());
        }
        if (successParameters.isEmpty()) {
            fail(ExaError.messageBuilder("E-VSSIT-5").message(
                    "None of the combinations that worked on a native Exasol table worked on the Virtual Schema table. Here is what was tried:\n{{queries|uq}}")
                    .parameter("queries", String.join("\n", failedQueries)).toString());
        }
        return successParameters;
    }

    private boolean assertSingleRunBehavesSameOnVirtualSchema(final String function, final Statement statement,
            final List<String> successParameters, final List<String> failedQueries,
            final ScalarFunctionLocalRun scalarFunctionLocalRun) {
        final String virtualSchemaQuery = this.virtualSchemaQueryBuilder
                .buildQueryFor(CALL_BUILDER.buildScalarFunctionCall(function, scalarFunctionLocalRun.getParameters()));
        try (final ResultSet actualResult = statement.executeQuery(virtualSchemaQuery)) {
            if (!areResultsIdentical(function, scalarFunctionLocalRun, virtualSchemaQuery, actualResult)) {
                return false;
            }
            successParameters.add(scalarFunctionLocalRun.getParameters());
        } catch (final SQLException exception) {
            failedQueries.add(virtualSchemaQuery);
            // ignore; probably just a strange parameter combination
        }
        return true;
    }

    private boolean areResultsIdentical(final String function, final ScalarFunctionLocalRun scalarFunctionLocalRun,
            final String virtualSchemaQuery, final ResultSet actualResult) {
        try {
            assertThat(actualResult, table().row(buildMatcher(scalarFunctionLocalRun.getResult())).matches());
        } catch (final AssertionError assertionError) {
            LOGGER.log(Level.SEVERE, ExaError.messageBuilder("E-VSSIT-6")
                    .message("Different output for query {{query}}:\n{{error}}\n", virtualSchemaQuery,
                            assertionError.getMessage())
                    .mitigation("You can exclude combination by adding {{exclude}} to your dialect specific excludes.",
                            ScalarFunctionsTestBase.getExcludeKey(function, scalarFunctionLocalRun))
                    .toString());
            return false;
        }
        return true;
    }

    /**
     * Quick check if the function behaves same on the virtual schema as it did on the Exasol table.
     * <p>
     * In contrast to {@link #assertFunctionBehavesSameOnVirtualSchema(String, List, Statement)} this function does not
     * check each parameter combination in a separate query but combines them into a single query. By that this function
     * is a lot fast. On the other hand, if a single combination leads to an error, the whole query fails. So you can
     * only use this function as a shortcut if it returns true.
     * </p>
     *
     * @param runsOnExasol Exasol runs (parameter - result pairs) to compare to
     * @param statement    statement to use.
     * @implNote The testing is executed in batches, since some databases have a limit in the amount of columns that can
     *           be queried in a single query.
     * @return {@code true} if all parameter combinations behaved same. {@code false} otherwise
     */
    boolean quickCheckIfFunctionBehavesSameOnVs(final String function, final List<ScalarFunctionLocalRun> runsOnExasol,
            final Statement statement) {
        for (int batchNr = 0; (batchNr * BATCH_SIZE) < runsOnExasol.size(); batchNr++) {
            final List<ScalarFunctionLocalRun> batch = runsOnExasol.subList(batchNr * BATCH_SIZE,
                    Math.min(runsOnExasol.size(), (batchNr + 1) * BATCH_SIZE));
            final String selectList = batch.stream()
                    .map(run -> CALL_BUILDER.buildScalarFunctionCall(function, run.getParameters()))
                    .collect(Collectors.joining(", "));
            final String virtualSchemaQuery = this.virtualSchemaQueryBuilder.buildQueryFor(selectList);
            try (final ResultSet actualResult = statement.executeQuery(virtualSchemaQuery)) {
                if (!buildResultMatcher(batch).matches(actualResult)) {
                    return false;
                }
                LOGGER.log(Level.FINE, "Quick check query was successful: {0}", virtualSchemaQuery);
            } catch (final SQLException exception) {
                return false;
            }
        }
        LOGGER.fine("Quick check was successful");
        return true;
    }

    private Matcher<ResultSet> buildResultMatcher(final List<ScalarFunctionLocalRun> batch) {
        return table().row(batch.stream().map(ScalarFunctionLocalRun::getResult).map(this::buildMatcher).toArray())
                .matches();
    }

    private Matcher<Object> buildMatcher(final Object object) {
        return CellMatcherFactory.cellMatcher(object, TypeMatchMode.NO_JAVA_TYPE_CHECK, BigDecimal.valueOf(0.0001));
    }
}
