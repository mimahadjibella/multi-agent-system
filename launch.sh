#!/bin/bash

# ******************************************************************
# * This script launches a JADE platform with preconfigured agents *
# ******************************************************************

# Path to directory containing .class files
CLASSPATH="bin:lib/jade.jar"

# Set agent names
AGENT1="agent1"
AGENT2="agent2"
AGENT3="agent3"

# Set function name (choose either "Sin" or "Logarithmic")
FUNCTION_NAME="Sin"

# Set function parameters
MIN_VALUE=0
MAX_VALUE=100
DELTA_VALUE=0.1

# Responsible agent
RESPONSIBLE_AGENT="responsible"

# Defining agents
AGENTS="$AGENT1:sma.ComputeAgent;$AGENT2:sma.ComputeAgent;$AGENT3:sma.ComputeAgent;$RESPONSIBLE_AGENT:sma.TestParallelAgent($FUNCTION_NAME,$MIN_VALUE,$MAX_VALUE,$DELTA_VALUE)"

# Command to launch JADE with the graphical interface
java -cp "$CLASSPATH" jade.Boot -gui -agents "$AGENTS"
