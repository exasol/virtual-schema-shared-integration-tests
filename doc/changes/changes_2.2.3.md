# Virtual Schema Shared Integration Tests 2.2.3, released 2022-12-21

Code name: VSSIT

## Summary

We renamed the error codes from `VS-SIT` to `VSSIT` in order to be uniform with our other projects.

We also updated dependencies and removed the reference to the Exasol artifactory, since all required dependencies are now available on Maven Central.

## Features

* #26: Renamed error codes from `VS-SIT` to `VSSIT`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-java:16.1.2` to `16.2.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.2.0` to `6.4.1`
* Updated `com.exasol:test-db-builder-java:3.3.4` to `3.4.1`
* Updated `org.mockito:mockito-junit-jupiter:4.8.0` to `4.10.0`
* Added `org.slf4j:slf4j-simple:2.0.6`
* Updated `org.testcontainers:junit-jupiter:1.17.3` to `1.17.6`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.2` to `1.2.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.8.0` to `2.9.1`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.15` to `0.16`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.2.7` to `1.3.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.13.0`
