# Virtual Schema Shared Integration Tests 3.1.0, released 2026-10-16

Code name: Add Capabilities

## Summary

The scalar-function integration tests now use the additional SQL capabilities provided by `virtual-schema-common-java:18.1.0`.

When upgrading to this release, regenerate the scalar-function parameter cache in `src/test/resources/integration/scalarFunctionsParameterCache.yml` by running the integration tests locally. You might want to mark this file as generated.

The new aggregate functions are not covered by this shared test suite, which currently tests scalar functions only. Tests for aggregate functions will be added in [#53](https://github.com/exasol/virtual-schema-shared-integration-tests/issues/53).

## Features

* #52: Upgraded `virtual-schema-common-java` to `18.1.0` for additional SQL capabilities.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:virtual-schema-common-java:18.0.4` to `18.1.0`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.5` to `5.7.7`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`
