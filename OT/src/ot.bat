echo off

rem In order to be able to use the default settings in this script
rem (1) There has to be a jdk1.3 java in the PATH variable or the JAVA variable below
rem has to be set to a jdk1.3 java.
rem (2) The script has to be in its usual location in an CVS orac3 checkout or
rem export directory tree.
rem It has to be started from within its own directory or by double click.
rem 
rem Example:
rem 
rem   cd some_cvs_checkout_dir/orac3/OT/bin
rem   ot.bat ukirt
rem 
rem If your setup differs from the one above please change the below variables accordingly.

rem If there is no appropriate jdk1.3 java in your PATH then set one here. 
set JAVA=java 

rem Set server to ATC, HILO, HOME or DEPLOYED. 
set SERVER=ATC 

set CFG_DIRS=..\cfg

IF -h==%1 GOTO USAGE

set INTERNAL_FRAMES=-internalframes
IF -internalframes==%1    set INTERNAL_FRAMES=-internalframes
IF -nointernalframes==%1  set INTERNAL_FRAMES=-nointernalframes

set TELESCOPE=
IF ukirt==%1              set TELESCOPE=ukirt
IF jcmt==%1               set TELESCOPE=jcmt
IF ukirt==%2              set TELESCOPE=ukirt
IF jcmt==%2               set TELESCOPE=jcmt

IF NOT DEFINED TELESCOPE echo No valid telescope specified. Using ukirt.
IF NOT DEFINED TELESCOPE set TELESCOPE=ukirt

IF NOT EXIST %CFG_DIRS%\%TELESCOPE% echo Telescope configuration directory %TELESCOPE% not found in %CFG_DIRS%
IF NOT EXIST %CFG_DIRS%\%TELESCOPE% GOTO USAGE

set CFG_TELESCOPE_DIR=%CFG_DIRS%\%TELESCOPE%
set CLASSPATH=..\tools\jhtools.jar;..\tools\jhall.jar;..\tools\diva.jar;..\tools\graph.jar;..\tools\jacl.jar;..\tools\jdom-b4.jar;..\tools\jsky.jar;..\tools\junit.jar;..\tools\tcljava.jar;..\tools\xerces.jar;..\lib\gemini.jar;..\lib\orac.jar;..\lib\odb.jar;..\lib\ot.jar;%CFG_TELESCOPE_DIR%

echo on
%JAVA% -ms5m -mx50m -DSERVER=%SERVER% -Dot.cfgdir=%CFG_TELESCOPE_DIR%\ -Dot.resource.cfgdir=.\ -classpath %CLASSPATH% jsky.app.ot.OT %INTERNAL_FRAMES%
echo off

GOTO END

:USAGE
echo Usage: %0 [-h] [-[no]internalframes] ukirt
echo Usage: %0 [-h] [-[no]internalframes] jcmt

:END
echo OT finished.
pause
echo on

