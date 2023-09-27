<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Matcher for SQL Result Sets][0]                       | [MIT License][1]                 |
| [SnakeYAML][2]                                         | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                              | [MIT License][5]                 |
| [JUnit Jupiter (Aggregator)][6]                        | [Eclipse Public License v2.0][7] |
| [Hamcrest][8]                                          | [BSD License 3][9]               |
| [Common module of Exasol Virtual Schemas Adapters][10] | [The MIT License (MIT)][11]      |

## Test Dependencies

| Dependency                                                  | License               |
| ----------------------------------------------------------- | --------------------- |
| [Test containers for Exasol on Docker][12]                  | [MIT License][13]     |
| [SLF4J Simple Provider][14]                                 | [MIT License][15]     |
| [Testcontainers :: JUnit Jupiter Extension][16]             | [MIT][17]             |
| [Test Database Builder for Java][18]                        | [MIT License][19]     |
| [mockito-junit-jupiter][20]                                 | [The MIT License][21] |
| [Apache Derby Database Engine and Embedded JDBC Driver][22] | [Apache 2][3]         |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                              |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][26]                              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][26]                              |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][26]                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][3]                                     |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][26]                              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][26]             |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]                      |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][26]                              |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][26]                              |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][26]             |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][26]                              |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]                  |
| [Maven Failsafe Plugin][40]                             | [Apache-2.0][26]                              |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]              |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                             |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][3]                               |
| [Project keeper maven plugin][46]                       | [The MIT License][47]                         |
| [Maven Clean Plugin][48]                                | [The Apache Software License, Version 2.0][3] |
| [Maven Resources Plugin][49]                            | [The Apache Software License, Version 2.0][3] |
| [Maven JAR Plugin][50]                                  | [The Apache Software License, Version 2.0][3] |
| [Maven Install Plugin][51]                              | [The Apache Software License, Version 2.0][3] |
| [Maven Site Plugin 3][52]                               | [The Apache Software License, Version 2.0][3] |

[0]: https://github.com/exasol/hamcrest-resultset-matcher/
[1]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[2]: https://bitbucket.org/snakeyaml/snakeyaml
[3]: http://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: https://junit.org/junit5/
[7]: https://www.eclipse.org/legal/epl-v20.html
[8]: http://hamcrest.org/JavaHamcrest/
[9]: http://opensource.org/licenses/BSD-3-Clause
[10]: https://github.com/exasol/virtual-schema-common-java/
[11]: https://github.com/exasol/virtual-schema-common-java/blob/main/LICENSE
[12]: https://github.com/exasol/exasol-testcontainers/
[13]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[14]: http://www.slf4j.org
[15]: http://www.opensource.org/licenses/mit-license.php
[16]: https://java.testcontainers.org
[17]: http://opensource.org/licenses/MIT
[18]: https://github.com/exasol/test-db-builder-java/
[19]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[20]: https://github.com/mockito/mockito
[21]: https://github.com/mockito/mockito/blob/main/LICENSE
[22]: http://db.apache.org/derby/
[23]: http://sonarsource.github.io/sonar-scanner-maven/
[24]: http://www.gnu.org/licenses/lgpl.txt
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://www.apache.org/licenses/LICENSE-2.0.txt
[27]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[28]: https://www.mojohaus.org/flatten-maven-plugin/
[29]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[30]: https://maven.apache.org/surefire/maven-surefire-plugin/
[31]: https://www.mojohaus.org/versions/versions-maven-plugin/
[32]: https://basepom.github.io/duplicate-finder-maven-plugin
[33]: http://www.apache.org/licenses/LICENSE-2.0.html
[34]: https://maven.apache.org/plugins/maven-deploy-plugin/
[35]: https://maven.apache.org/plugins/maven-gpg-plugin/
[36]: https://maven.apache.org/plugins/maven-source-plugin/
[37]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[38]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[39]: http://www.eclipse.org/legal/epl-v10.html
[40]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[41]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[42]: https://www.eclipse.org/legal/epl-2.0/
[43]: https://github.com/exasol/error-code-crawler-maven-plugin/
[44]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[45]: http://zlika.github.io/reproducible-build-maven-plugin
[46]: https://github.com/exasol/project-keeper/
[47]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[48]: http://maven.apache.org/plugins/maven-clean-plugin/
[49]: http://maven.apache.org/plugins/maven-resources-plugin/
[50]: http://maven.apache.org/plugins/maven-jar-plugin/
[51]: http://maven.apache.org/plugins/maven-install-plugin/
[52]: http://maven.apache.org/plugins/maven-site-plugin/
