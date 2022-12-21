package com.exasol.adapter.commontests.scalarfunction;

import java.sql.*;
import java.util.*;

import com.exasol.errorreporting.ExaError;

/**
 * This class provides a set of scalar functions, supported by the Exasol database.
 */
public class ScalarFunctionProvider {
    /**
     * Get scalar functions supported by the Exasol database.
     * 
     * @param exasolConnection connection to an Exasol database.
     * @return set of supported function
     */
    public Set<String> getScalarFunctions(final Connection exasolConnection) {
        try (final Statement statement = exasolConnection.createStatement();
                final ResultSet resultSet = statement.executeQuery(
                        "SELECT PARAM_VALUE FROM EXA_METADATA WHERE PARAM_NAME IN('timeDateFunctions', 'stringFunctions', 'systemFunctions', 'numericFunctions', 'SQL92StringFunctions','SQL92NumericValueFunctions')")) {
            return parseScalarFunctions(resultSet);
        } catch (final SQLException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSSIT-7")
                    .message("Failed to fetch list of scalar functions from the exasol database.").ticketMitigation()
                    .toString(), exception);
        }
    }

    private Set<String> parseScalarFunctions(final ResultSet resultSet) throws SQLException {
        final Set<String> functions = new HashSet<>();
        while (resultSet.next()) {
            functions.addAll(Arrays.asList(resultSet.getString(1).split(",")));
        }
        correctFunctionNames(functions);
        return functions;
    }

    /**
     * For some reasons some functions art listed with a wrong name in the meta-table. In this method we correct this.
     * 
     * @param functions set of functions to correct
     */
    private void correctFunctionNames(final Set<String> functions) {
        functions.remove("trimLeading");
        functions.add("ltrim");
        functions.remove("trimBoth");
        functions.add("trim");
        functions.remove("trimTrailing");
        functions.add("rtrim");
    }
}
