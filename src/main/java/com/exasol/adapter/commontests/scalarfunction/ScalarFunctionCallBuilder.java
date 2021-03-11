package com.exasol.adapter.commontests.scalarfunction;

/**
 * This class builds scalar function calls.
 */
public class ScalarFunctionCallBuilder {

    /**
     * Build a scalar function call.
     * 
     * @param function   function name
     * @param parameters parameters
     * @return scalar function call
     */
    public String buildScalarFunctionCall(final String function, final String parameters) {
        return function + "(" + String.join(", ", parameters) + ")";
    }
}
