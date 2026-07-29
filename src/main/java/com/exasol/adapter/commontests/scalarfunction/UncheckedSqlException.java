package com.exasol.adapter.commontests.scalarfunction;

import java.sql.SQLException;

class UncheckedSqlException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    UncheckedSqlException(final SQLException cause) {
        super(cause.getMessage(), cause);
    }

    UncheckedSqlException(final String message, final SQLException cause) {
        super(message, cause);
    }
}
