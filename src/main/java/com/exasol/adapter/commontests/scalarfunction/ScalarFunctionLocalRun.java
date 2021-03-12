package com.exasol.adapter.commontests.scalarfunction;

/**
 * This class represents a run of a scalar function on a local exasol table.
 */
class ScalarFunctionLocalRun {
    private final String parameters;
    private final Object result;

    /**
     * Create a new instance of {@link ScalarFunctionLocalRun}.
     *
     * @param parameters parameters that were passed to the scalar function
     * @param result     result of the scalar function run
     */
    ScalarFunctionLocalRun(final String parameters, final Object result) {
        this.parameters = parameters;
        this.result = result;
    }

    /**
     * Get the parameters that were passed to the scalar function
     *
     * @return string containing the parameters
     */
    String getParameters() {
        return this.parameters;
    }

    /**
     * Get the result of the scalar function run
     *
     * @return result
     */
    Object getResult() {
        return this.result;
    }

    @Override
    public String toString() {
        return "ScalarFunctionLocalRun [parameters=" + this.parameters + ", result=" + this.result + "]";
    }
}
