@echo off
	
:: Eclipse home. Update this to match your versions
set ECLIPSE_HOME=C:\eclipse-SDK\eclipse-GANYMEDE
set LAUNCHER=org.eclipse.equinox.launcher_1.0.100.v20080509-1800.jar
set PDE_BUILD=org.eclipse.pde.build_3.4.0.v20080604

:: Build directory
set BUILD_DIR=C:\tmp\build

:: Configuration directory
set BUILD_CONF=C:\Documents\Books\EclipseRCP\Workspace\ch10.RelEng

set MAIN=org.eclipse.core.launcher.Main
set APP=org.eclipse.ant.core.antRunner
set CP=%ECLIPSE_HOME%/plugins/%LAUNCHER%

set BUILDFILE=%ECLIPSE_HOME%/plugins/%PDE_BUILD%/scripts/productBuild/productBuild.xml
set ARGS=-DbaseLocation=%ECLIPSE_HOME% -DbuildDirectory=%BUILD_DIR% -Dbuilder=%BUILD_CONF% 

java -cp %CP% %MAIN% %ARGS% -application %APP%  -buildfile %BUILDFILE%

