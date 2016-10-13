@echo off

:: Change this to fit your system	
:: Eclipse Installation
set ECLIPSE_HOME=C:\eclipse-SDK\eclipse-GANYMEDE

:: Build master folder
set ROOT_BUILD=C:\Documents\Books\EclipseRCP\Workspace\ch10.Local.Build

:: Don't change this...
:: Name of the Equinox launcher plugin under ECLIPSE_HOME/plugins
set LAUNCHER=org.eclipse.equinox.launcher_1.0.100.v20080509-1800.jar

:: Eclipse plug-in that contains the PDE build scripts
set PDE_BUILD=org.eclipse.pde.build_3.4.0.v20080604

:: Launch class path
set CP=%ECLIPSE_HOME%/plugins/%LAUNCHER%

:: Main Program 
set MAIN=org.eclipse.core.launcher.Main
set APP=org.eclipse.ant.core.antRunner

:: Product Build file
set BUILDFILE=%ECLIPSE_HOME%/plugins/%PDE_BUILD%/scripts/productBuild/productBuild.xml

:: Build directory. It must contain the user developed plugins/features
set BUILD_DIR=%ROOT_BUILD%\buildDir

:: Directory that has build.properties
set BUILD_CONF=%ROOT_BUILD%\buildConfig

set ARGS=-DbaseLocation=%ECLIPSE_HOME% -DbuildDirectory=%BUILD_DIR% -Dbuilder=%BUILD_CONF% 

java -cp %CP% %MAIN% %ARGS% -application %APP%  -buildfile %BUILDFILE%
