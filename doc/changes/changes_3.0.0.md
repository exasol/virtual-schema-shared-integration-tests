# Virtual Schema Shared Integration Tests 3.0.0, released 2024-02-21

Code name: testScalarFunctions directly executable in test runners.

## Summary

Makes `testScalarFunctions` directly executable in test runners.
Previously you had to run the encompassing derived integration test class based on `ScalarFunctionsTestBase`.

- The nested class is now removed.
- The setup and teardown of the required infrastructure in the derived classes now needs to be moved to the abstract `beforeAllSetup` and `afterAllTeardown` methods that need to be implemented.
- This is a breaking change.

## Features

- #36: Make testScalarFunctions directly executable in test runners.

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.16` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
