# Virtual Schema Shared Integration Tests 2.2.6, released 2023-11-21

Code name: Fix vulnerabilities CVE-2023-4043 and CVE-2022-46337 in test dependencies

## Summary

This release fixes vulnerabilities CVE-2023-4043 and CVE-2022-46337 in test dependencies:

* `org.eclipse.parsson:parsson`: CVE-2023-4043 CWE-20: Improper Input Validation (7.5)

**Note:** This release excludes vulnerability CVE-2022-46337 in `org.apache.derby:derby:jar:10.14.2.0` which is required only for tests. Newer versions don’t support Java 8 any more.

We also run integration tests now with both Exasol DB version 7.1 and 8.

## Security

* #34: Fixed vulnerabilities CVE-2023-4043 and CVE-2022-46337 in test dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.6.1` to `1.6.3`
* Updated `com.exasol:virtual-schema-common-java:17.0.0` to `17.0.1`
* Updated `org.junit.jupiter:junit-jupiter:5.10.0` to `5.10.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.2` to `7.0.0`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.2`
* Updated `org.mockito:mockito-junit-jupiter:5.5.0` to `5.7.0`
* Updated `org.testcontainers:junit-jupiter:1.19.0` to `1.19.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.12` to `2.9.16`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
