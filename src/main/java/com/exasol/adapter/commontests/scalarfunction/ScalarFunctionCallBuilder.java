package com.exasol.adapter.commontests.scalarfunction;

/**
 * This class builds scalar function calls.
 */
public class ScalarFunctionCallBuilder {

    public String buildFunctionCall(final String function, final String parameters) {
        return function + "(" + String.join(", ", parameters) + ")";
    }
}
