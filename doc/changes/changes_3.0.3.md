# Virtual Schema Shared Integration Tests 3.0.3, released 2026-07-29

Code name: Improve error handling

## Summary

This release improves error handling and logging to simplify debugging failing tests.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.3`
* Updated `com.exasol:virtual-schema-common-java:17.1.0` to `18.0.3`
* Updated `org.junit.jupiter:junit-jupiter-api:5.12.2` to `5.14.4`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.14.4`
* Updated `org.yaml:snakeyaml:2.4` to `2.6`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.7` to `8.0.1`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `4.0.2`
* Updated `org.mockito:mockito-junit-jupiter:5.17.0` to `5.23.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`
* Removed `org.testcontainers:junit-jupiter:1.21.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.4` to `2.1.0`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.3` to `5.7.4`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.15.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.7` to `3.2.8`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.2` to `3.12.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.21.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.7.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.4`
