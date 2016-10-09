


::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                            SET ENVIRONMENT VARS                            ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set VERSION=${project.version}

:: Force cd to where this batch resides, this allow to execute this script using
:: shortcuts.
for /f %%i in ("%0") do set curpath=%%~dpi 
cd /d %curpath% 

:: Set APP_HOME assuming that this script is executed in a sibbling folder 
:: just one level below %APP_HOME% such as bin, script or any other.
:: Trailing back-slash trimmed if any.
cd /d ..
set APP_HOME=%CD%
IF "%CD:~-1%"=="\" SET APP_HOME=%CD:~0,-1% 
echo %APP_HOME%

:: Set log4j configuration file.
cd /d config
set LOG4J_CONFIG=%CD%\log4j2.xml

:: Restores previous path
cd /d %curpath% 

:: java.home is replaced by maven filters when package is executed.
cd /d ..\..\java\jdk1.8.0_45 
set APP_JAVA_HOME= %CD% 
IF "%CD:~-1%"=="\" SET APP_JAVA_HOME=%CD:~0,-1% 
set APP_JAVA_HOME=%APP_JAVA_HOME:\\=\%
echo %APP_JAVA_HOME%

:: Restores previous path
cd /d %curpath% 

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
::                              STARTS SERVER                                 ::
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Launch java command to run the server
cd /d %APP_JAVA_HOME%\bin\
java -Dlog4j.configurationFile=file://%LOG4J_CONFIG% -jar %APP_HOME%\lib\${artifact.artifactId}-${artifact.version}.jar %APP_HOME%\config\client-properties.xml 

:: Do not close console until user press a key. This prevents closing the
:: console window when double-clicking this batch in a windows explorer 
:: pause
