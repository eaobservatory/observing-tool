echo off

rem If there is no appropriate jdk1.3 java in your PATH then set one here. 
set JAVA=java 

rem This assumes that all cfg, class, images and jar are in ORAC_BASE.
rem If this is not the case some of the settings further down in the script might need editing.
set ORAC_BASE=..\..
set CFG_DIRS=%ORAC_BASE%\OT\install\cfg

IF -h==%1 GOTO USAGE

set INTERNAL_FRAMES=-internalframes
IF -internalframes==%1    set INTERNAL_FRAMES=-internalframes
IF -nointernalframes==%1  set INTERNAL_FRAMES=-nointernalframes

set TELESCOPE=
IF ukirt==%1              set TELESCOPE=ukirt
IF jcmt==%1               set TELESCOPE=jcmt
IF ukirt==%2              set TELESCOPE=ukirt
IF jcmt==%2               set TELESCOPE=jcmt

IF NOT DEFINED TELESCOPE GOTO USAGE

IF NOT EXIST %CFG_DIRS%\%TELESCOPE% echo Telescope configuration directory %TELESCOPE% not found in %CFG_DIRS%
IF NOT EXIST %CFG_DIRS%\%TELESCOPE% GOTO USAGE

set CFG_TELESCOPE_DIR=%CFG_DIRS%\%TELESCOPE%

echo on
%JAVA% -ms5m -mx50m -DSERVER=ATC -Dot.cfgdir=%CFG_TELESCOPE_DIR%\ -Dot.resource.cfgdir=.\ -classpath  %ORAC_BASE%\OT\install\classes;%ORAC_BASE%\GEMINI\install\classes;%ORAC_BASE%\ORAC\install\classes;%ORAC_BASE%\ODB\install\classes;%ORAC_BASE%\OT\tools\jsky.jar;%ORAC_BASE%\OT\tools\diva.jar;%ORAC_BASE%\OT\tools\graph.jar;%ORAC_BASE%\OT\tools\jacl.jar;%ORAC_BASE%\OT\tools\jdom-b4.jar;%ORAC_BASE%\OT\tools\junit.jar;%ORAC_BASE%\OT\tools\tcljava.jar;%ORAC_BASE%\OT\tools\xerces.jar;%ORAC_BASE%\ORAC\tools\jhall.jar;%ORAC_BASE%\ORAC\tools\jhtools.jar;%CFG_TELESCOPE_DIR% jsky.app.ot.OT %INTERNAL_FRAMES%
echo off

GOTO END

:USAGE
echo Usage: %0 [-h] [-[no]internalframes] ukirt
echo Usage: %0 [-h] [-[no]internalframes] jcmt

:END
echo on

