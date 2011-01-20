@ECHO off
set WINDIFF="C:\Program Files (x86)\Microsoft SDKs\Windows\v7.0A\Bin\x64\WinDiff.Exe"
set OUTFILE=%~n0.txt
if EXIST %OUTFILE% del %OUTFILE%

rem goto src
rem goto tests

:src

set ROOT_LEFT=..\SeattleBusBot\src\com\joulespersecond\oba
set ROOT_RIGHT=src\org\onebusaway\berry\api

for %%i in (%ROOT_LEFT%\*.java) do (
  echo %%i %ROOT_RIGHT%\%%~nxi>>%OUTFILE%
)

for /d %%i in (%ROOT_LEFT%\*.*) do (
  for %%j in (%%i\*.*) do (
    rem echo j=%%j
    echo %%j %ROOT_RIGHT%\%%~ni\%%~nxj>>%OUTFILE%
  )
)

:tests

set ROOT_LEFT=..\SeattleBusBot\tests\src\com\joulespersecond\oba
set ROOT_RIGHT=..\OneBusAwayBerryTests\src\org\onebusaway\berry\test\api

for %%i in (%ROOT_LEFT%\*.java) do (
  rem echo i=%%i
  echo %%i %ROOT_RIGHT%\%%~nxi>>%OUTFILE%
)

for /d %%i in (%ROOT_LEFT%\*.*) do (
  rem echo i=%%i
  for %%j in (%%i\test\*.*) do (
    echo j=%%j
    echo %%j %ROOT_RIGHT%\%%~ni\test\%%~nxj>>%OUTFILE%
  )
)


:start

rem notepad %OUTFILE%
start "windiff" %WINDIFF% -I %OUTFILE%

