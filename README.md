# Virtual Schema Shared Integration Tests

[![Build Status](https://github.com/exasol/virtual-schema-shared-integration-tests/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/virtual-schema-shared-integration-tests/actions/workflows/ci-build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.exasol/virtual-schema-shared-integration-tests)](https://search.maven.org/artifact/com.exasol/virtual-schema-shared-integration-tests)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-shared-integration-tests&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-shared-integration-tests)

This repository contains abstract integration tests for Exasol Virtual Schemas.

The tests check that Exasol functions behave in the same way on a Virtual Schema table and on a regular Exasol table.

## Usage

You can use these tests by subclassing `ScalarFunctionsTestBase` in your project's tests. By that your test will inherit the shared tests.

### Excludes

For some dialects some test might need not apply. That's why this test suite allows you to exclude certain tests:

```
@Override
public Set<String> getDialectSpecificExcludes(){
    return Set.of("neg");
}
```

For the automatic tests you can also exclude certain parameter combinations.

## Additional Information

* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)
