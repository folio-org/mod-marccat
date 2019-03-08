#!/usr/bin/env bash
mvn sonar:sonar \
-Dsonar.projectKey=atcult_mod-marccat \
-Dsonar.organization=atcult \
-Dsonar.host.url=https://sonarcloud.io \
-Dsonar.login=16ecd4e9f0be9249627521ea7013d246f023bf5d

#mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar
#-Dsonar.host.url=https://sonarcloud.io
#-Dsonar.organization=folio-org
#-Dsonar.verbose=true
