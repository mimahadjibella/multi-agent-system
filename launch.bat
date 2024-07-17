@echo off

rem ******************************************************************
rem * This script launches a JADE platform with preconfigured agents *
rem ******************************************************************

rem Path to directory containing .class files
set CLASSPATH=bin;lib/jade.jar

rem Set agent names
set AGENT1="agent1"
set AGENT2="agent2"
set AGENT3="agent3"

rem Set function name (choose either "Sin" or "Logarithmic")
set FUNCTION_NAME="Sin"

rem Set function parameters 
set MIN_VALUE=0
set MAX_VALUE=100
set DELTA_VALUE=0.1

rem Responsible agent
set RESPONSIBLE_AGENT="responsible"

rem Defining agents
set AGENTS="%AGENT1%:sma.ComputeAgent;%AGENT2%:sma.ComputeAgent;%AGENT3%:sma.ComputeAgent;%RESPONSIBLE_AGENT%:sma.TestParallelAgent(%FUNCTION_NAME%,%MIN_VALUE%,%MAX_VALUE%,%DELTA_VALUE%)"

rem Command to launch JADE with the graphical interface
java -cp "%CLASSPATH%" jade.Boot -gui -agents %AGENTS%
