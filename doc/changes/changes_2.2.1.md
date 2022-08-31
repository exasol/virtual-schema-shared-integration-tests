# virtual-schema-shared-integration-tests 2.2.1, released 2022-08-31

Code name: 2.2.1: Upgrade dependencies compared to 2.2.0

## Summary

This release upgrades dependencies and reduces the number of runtime dependencies, fixing [CVE-2022-21724](https://ossindex.sonatype.org/vulnerability/0f319d1b-e964-4471-bded-db3aeb3c3a29?component-type=maven&component-name=org.postgresql.postgresql&utm_source=ossindex-client&utm_medium=integration&utm_content=1.1.1) in the PostgreSQL JDBC driver.

## Features

* #20: Upgraded dependencies to fix [CVE-2022-21724](https://ossindex.sonatype.org/vulnerability/0f319d1b-e964-4471-bded-db3aeb3c3a29?component-type=maven&component-name=org.postgresql.postgresql&utm_source=ossindex-client&utm_medium=integration&utm_content=1.1.1) in the PostgreSQL JDBC driver.

## Bugfixes

* #22: Fixed broken links checker

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:virtual-schema-common-java:15.2.0` to `15.3.3`
* Updated `org.junit.jupiter:junit-jupiter:5.8.1` to `5.9.0`
* Updated `org.yaml:snakeyaml:1.29` to `1.31`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:5.1.1` to `6.2.0`
* Updated `com.exasol:test-db-builder-java:3.2.1` to `3.3.4`
* Updated `org.mockito:mockito-junit-jupiter:4.1.0` to `4.7.0`
* Updated `org.testcontainers:junit-jupiter:1.16.2` to `1.17.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.2` to `2.6.2`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.13` to `0.15`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.8.1` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3` to `3.0.0-M5`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3` to `3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.8.1` to `2.10.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Updated `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0` to `3.2.0`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8` to `1.6.13`
