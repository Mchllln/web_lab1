@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "__MVNW_ARG0_NAME__=%~nx0")
@SET ___MVNW_Jackson_BIND_ERROR_=
@SET __MVNW_LAUNCHER_CLASS_NAME__=org.apache.maven.wrapper.MavenWrapperMain
@SET __MVNW_LAUNCHER_CLASS__=%__MVNW_LAUNCHER_CLASS_NAME__%

@SETLOCAL
@SET MAVEN_PROJECTBASEDIR=%~dp0
@SET MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar
@SET MAVEN_WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties

@IF NOT "%MVNW_REPOURL%"=="" SET "MVNW_REPOURL=%MVNW_REPOURL%"

FOR /F "usebackq delims==" %%a IN ("%MAVEN_WRAPPER_PROPERTIES%") DO (
    IF "%%a"=="distributionUrl" SET "DISTRIBUTION_URL=%%b"
    IF "%%a"=="wrapperUrl" SET "WRAPPER_URL=%%b"
)

@IF NOT EXIST "%MAVEN_WRAPPER_JAR%" (
    IF NOT "%WRAPPER_URL%"=="" (
        @SET DOWNLOAD_URL=%WRAPPER_URL%
        @ECHO Downloading %DOWNLOAD_URL%
        powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%MAVEN_WRAPPER_JAR%')"
        IF ERRORLEVEL 1 (
            ECHO Could not download maven-wrapper.jar, try to use mvnw.cmd with MVNW_REPOURL environment variable
            EXIT /B 1
        )
    ) ELSE (
        ECHO ------------------------------------------------------------
        ECHO No Maven Wrapper JAR found. Please run:
        ECHO     mvn wrapper:generate
        ECHO ------------------------------------------------------------
        EXIT /B 1
    )
)

@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_URL="%DISTRIBUTION_URL%"

@java %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath "%MAVEN_WRAPPER_JAR%" ^
"-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
%WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
@IF ERRORLEVEL 1 EXIT /B 1
