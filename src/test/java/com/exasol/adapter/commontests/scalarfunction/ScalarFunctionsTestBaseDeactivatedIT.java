package com.exasol.adapter.commontests.scalarfunction;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is a test for {@link ScalarFunctionsTestBase} that skips all tests.
 */
public class ScalarFunctionsTestBaseDeactivatedIT extends ScalarFunctionsTestBaseIT {
    @Override
    public Set<String> getDialectSpecificExcludes() {
        return Set.of("SYSTIMESTAMP", "RAND", "SYS_GUID", "ADD", "SUB", "MULT", "FLOAT_DIV", "CAST", "GREATEST",
                "ROUND", "NEG", "CASE", "CONVERT_TZ", "DATE_TRUNC", "NUMTODSINTERVAL", "NUMTOYMINTERVAL",
                "TO_YMINTERVAL", "TO_DSINTERVAL", "JSON_VALUE", "position", "EXTRACT", "POSIX_TIME",
                //
                "localtimestamp", "sysdate", "current_schema", "current_statement", "current_session", "current_date",
                "current_user", "current_timestamp",
                //
                "acos", "add_days", "add_hours", "add_minutes", "add_months", "add_seconds", "add_weeks", "add_years",
                "ascii", "asin", "atan", "atan2", "bit_Length", "bit_length", "ceil", "ceiling", "char", "char_Length",
                "char_length", "character_Length", "character_length", "chr", "concat", "cos", "cosh", "cot", "curdate",
                "current_schema_id", "day", "days_between", "degrees", "div", "dump", "exp", "floor", "from_posix_time",
                "hour", "hours_between", "insert", "instr", "iproc", "is_number", "lcase", "least", "left", "length",
                "ln", "locate", "log", "log10", "log2", "lower", "lpad", "mid", "minute", "minutes_between", "mod",
                "modmonth", "months_between", "nproc", "octet_Length", "octet_length", "pi", "powerradians",
                "regexp_instr", "regexp_replace", "regexp_substr", "repeat", "replace", "reverse", "right", "round",
                "rpad", "rtrimsecondseconds_between", "sign", "sin", "sinh", "soundex", "space", "sqrt", "substr",
                "substring", "tan", "tanh", "to_char", "to_date", "to_number", "to_timestamp", "translate", "trimtrunc",
                "truncate", "ucase", "unicode", "unicodechr", "upper", "user", "week", "year", "years_between", "trim",
                "trunc", "power", "ltrim", "second", "month", "radians", "rtrim", "seconds_between").stream()
                .map(String::toLowerCase).collect(Collectors.toSet());
    }
}
