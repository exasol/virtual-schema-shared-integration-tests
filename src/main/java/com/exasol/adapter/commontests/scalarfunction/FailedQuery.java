package com.exasol.adapter.commontests.scalarfunction;

class FailedQuery {
    private final String query;
    private final Exception exception;

    FailedQuery(final String query, final Exception exception) {
        this.query = query;
        this.exception = exception;
    }

    public String getQuery() {
        return this.query;
    }

    public String getExceptionMessage() {
        return this.exception.getMessage();
    }

    public Exception getException() {
        return this.exception;
    }
}
