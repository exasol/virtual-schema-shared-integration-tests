package com.exasol.adapter.commontests.scalarfunction;

/**
 * This class represents a run of a scalar function on a local exasol table.
 */
class ExasolRun {
    private final String parameters;
    private final Object result;

    ExasolRun(final String parameters, final Object result) {
        this.parameters = parameters;
        this.result = result;
    }

    String getParameters() {
        return this.parameters;
    }

    Object getResult() {
        return this.result;
    }
}
