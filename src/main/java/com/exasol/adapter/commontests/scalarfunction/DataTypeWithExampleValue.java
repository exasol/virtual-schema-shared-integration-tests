package com.exasol.adapter.commontests.scalarfunction;

import com.exasol.adapter.metadata.DataType;

class DataTypeWithExampleValue {
    private final DataType exasolDataType;
    private final Object exampleValue;

    DataTypeWithExampleValue(final DataType exasolDataType, final Object exampleValue) {
        this.exasolDataType = exasolDataType;
        this.exampleValue = exampleValue;
    }

    public DataType getExasolDataType() {
        return this.exasolDataType;
    }

    public Object getExampleValue() {
        return this.exampleValue;
    }
}
