# Virtual Schema Shared Integration Tests 3.0.2, released 2025-??-??

Code name: Fixed vulnerability CVE-2025-48924 in org.apache.commons:commons-lang3:jar:3.16.0:test

## Summary

This release fixes the following vulnerability:

### CVE-2025-48924 (CWE-674) in dependency `org.apache.commons:commons-lang3:jar:3.16.0:test`
Uncontrolled Recursion vulnerability in Apache Commons Lang.

This issue affects Apache Commons Lang: Starting withÂ commons-lang:commons-langÂ 2.0 to 2.6, and, from org.apache.commons:commons-lang3 3.0 beforeÂ 3.18.0.

The methods ClassUtils.getClass(...) can throwÂ StackOverflowError on very long inputs. Because an Error is usually not handled by applications and libraries, a 
StackOverflowError couldÂ cause an application to stop.

Users are recommended to upgrade to version 3.18.0, which fixes the issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-48924?component-type=maven&component-name=org.apache.commons%2Fcommons-lang3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-48924
* https://github.com/advisories/GHSA-j288-q9x7-2f5v

## Security

* #47: Fixed vulnerability CVE-2025-48924 in dependency `org.apache.commons:commons-lang3:jar:3.16.0:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.1`
* Updated `org.junit.jupiter:junit-jupiter-api:5.12.2` to `5.13.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.3`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.4` to `7.1.6`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.2`
* Updated `org.apache.derby:derby:10.15.2.0` to `10.17.1.0`
* Updated `org.mockito:mockito-junit-jupiter:5.17.0` to `5.18.0`
* Updated `org.testcontainers:junit-jupiter:1.21.0` to `1.21.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.0.1` to `5.2.2`
