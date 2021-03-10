package com.exasol.adapter.commontests.scalarfunction;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class searches for parameter combinations that do not cause an exception during the execution of scalar
 * functions on a local Exasol table.
 */
public class ScalarFunctionParameterFinder {
    private static final ScalarFunctionCallBuilder FUNCTION_CALL_BUILDER = new ScalarFunctionCallBuilder();
    private static final Logger LOGGER = Logger.getLogger(ScalarFunctionParameterFinder.class.getName());
    private static final int MAX_NUM_PARAMETERS = 4;
    /**
     * One list for each level of parameters, that contains a list of parameter combinations. Each parameter combination
     * is a string with comma separated parameters.
     */
    private final List<List<String>> parameterCombinationsCache;
    private final ScalarFunctionQueryBuilder localQueryBuilder;
    private final ScalarFunctionsParameterCache parameterCache;

    /**
     * Create a new instance of {@link ScalarFunctionParameterFinder}.
     * 
     * @param availableColumns  list of available columns in the test table
     * @param localQueryBuilder query builder that builds queries on a regular Exasol table
     * @param parameterCache    disc cache for the parameters
     */
    public ScalarFunctionParameterFinder(final Collection<String> availableColumns,
            final ScalarFunctionQueryBuilder localQueryBuilder, final ScalarFunctionsParameterCache parameterCache) {
        this.parameterCombinationsCache = generateParameterCombinations(availableColumns);
        this.localQueryBuilder = localQueryBuilder;
        this.parameterCache = parameterCache;
    }

    /**
     * Permute the literals.
     *
     * @return permutations
     */
    private static List<List<String>> generateParameterCombinations(final Collection<String> availableColumns) {
        final List<List<String>> combinations = new ArrayList<>(MAX_NUM_PARAMETERS + 1);
        combinations.add(List.of(""));
        for (int numParameters = 1; numParameters <= MAX_NUM_PARAMETERS; numParameters++) {
            final List<String> previousIterationParameters = combinations.get(numParameters - 1);
            combinations.add(generateCombination(availableColumns, previousIterationParameters));
        }
        return combinations;
    }

    private static List<String> generateCombination(final Collection<String> availableColumns,
            final List<String> previousIterationParameters) {
        return previousIterationParameters.stream()//
                .flatMap(addPermutation(availableColumns)).collect(Collectors.toList());
    }

    private static Function<String, Stream<? extends String>> addPermutation(
            final Collection<String> availableColumns) {
        return smallerCombination -> availableColumns.stream().map(literal -> join(smallerCombination, literal));
    }

    private static String join(final String smallerCombination, final String literal) {
        if (smallerCombination.isEmpty() || literal.isEmpty()) {
            return smallerCombination + literal;
        } else {
            return smallerCombination + ", " + literal;
        }
    }

    /**
     * Find parameter combinations for a scalar function that do not throw an exception on a regular exasol table.
     * <p>
     * If the disc cache contains combinations for this scalar function, this method only returns these. This is an
     * performance optimization, since the cache only contains the runs, that lead to a success on the last run.
     * </p>
     * 
     * @param function  scalar function
     * @param statement connection to the exasol database
     * @return list of successful executions
     */
    public List<ScalarFunctionLocalRun> findOrGetFittingParameters(final String function, final Statement statement) {
        if (this.parameterCache.hasParametersForFunction(function)) {
            LOGGER.log(Level.FINE, "Using parameters from parameter cache for function {0}.", function);
            return findFittingParameters(function,
                    this.parameterCache.getFunctionsValidParameterCombinations(function).stream(), statement);
        } else {
            LOGGER.log(Level.FINE, "Using generated parameters for function {0}.", function);
            return findFittingParameters(function, statement);
        }
    }

    /**
     * Try to find fitting parameters combinations by permuting a set of columns and try if they produce an exception on
     * a regular Exasol table.
     * <p>
     * This method first tries to find fitting combinations with only 3 columns and only if that has no results,
     * increases the number of columns (which leads to an exponential growth of possible combinations).
     * </p>
     *
     * @param function  function to test
     * @param statement exasol statement
     * @return list of successful runs
     */
    private List<ScalarFunctionLocalRun> findFittingParameters(final String function, final Statement statement) {
        final int fastThreshold = 3; // with three parameters the search is still fast; with 4 it gets slow
        final List<List<String>> fastCombinationLists = this.parameterCombinationsCache.subList(0, fastThreshold + 1);
        final List<ScalarFunctionLocalRun> fastParameters = findFittingParameters(function,
                fastCombinationLists.stream().flatMap(Collection::stream), statement);
        if (!fastParameters.isEmpty()) {
            return fastParameters;
        } else {
            return findCostyFittingParameters(function, statement, fastThreshold);
        }
    }

    private List<ScalarFunctionLocalRun> findCostyFittingParameters(final String function, final Statement statement,
            final int fastThreshold) {
        for (int numParameters = fastThreshold + 1; numParameters <= MAX_NUM_PARAMETERS; numParameters++) {
            final List<ScalarFunctionLocalRun> result = findFittingParameters(function,
                    this.parameterCombinationsCache.get(numParameters).stream(), statement);
            if (!result.isEmpty()) {
                return result;
            }
        }
        return Collections.emptyList();
    }

    private List<ScalarFunctionLocalRun> findFittingParameters(final String function,
            final Stream<String> possibleParameters, final Statement statement) {
        return possibleParameters
                .map(parameterCombination -> this.runFunctionOnExasol(function, parameterCombination, statement))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private ScalarFunctionLocalRun runFunctionOnExasol(final String function, final String parameters,
            final Statement statement) {
        final String functionCall = FUNCTION_CALL_BUILDER.buildScalarFunctionCall(function, parameters);
        try (final ResultSet expectedResult = statement
                .executeQuery(this.localQueryBuilder.buildQueryFor(functionCall))) {
            expectedResult.next();
            return new ScalarFunctionLocalRun(parameters, expectedResult.getObject(1));
        } catch (final SQLException exception) {
            return null;
        }
    }
}
