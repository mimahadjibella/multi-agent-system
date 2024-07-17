# Project Multi-Agent System

1. [Introduction](#introduction)
2. [Program sequence](#program-sequence)
    1. [ComputeAgent](#computeagent)
    2. [TestParallelAgent](#testparallelagent)
3. [Documentation generation](#documentation-generation)
4. [Application launch](#application-launch)
    1. [On Windows (launch.bat)](#on-windows-launchbat)
    2. [On Linux (launch.sh)](#on-linux-launchsh)
    3. [Examples](#examples)
5. [Project delivered](#project-delivered)
    1. [Date](#date)
    2. [Deliverables](#deliverables)
6. [Remarks](#remarks)
7. [Authors](#authors)

## Introduction

The aim of this project is to create a distributed application based on JADE agents for calculating an integral, using a method similar to that of Simpson. The agents are divided into two main types: the ComputeAgent, responsible for executing the computations, and the TestParallelAgent, responsible for coordinating the ComputeAgents to perform parallel computations.

## Program sequence 
The scenario involves two types of agent: ComputeAgent and TestParallelAgent.
### ComputeAgent 
* The ComputeAgent expects integral calculation requests.
* When a request is received, it calculates the integral according to the parameters received 
* Sends the result of the integral to the sender of the request.
### TestParallelAgent
* TestParallelAgent searches for all available ComputeAgents.
* It divides the integration interval into as many parts as it has found ComputeAgents.
* Sends each ComputeAgent its share of the calculation.
* Waits for results from all ComputeAgents, sums them up.
* Compare the result obtained by calculating the sum total alone with the result obtained in parallel.
* Measures the time taken to obtain the result in monothread and in parallel.

## Documentation generation
Project documentation is automatically generated and stored in the docs folder. To regenerate the documentation, use the following command:
```bash
javadoc -d docs -cp "lib/jade.jar" -sourcepath src -subpackages sma
```

## Application launch
Before running the application, make sure you have the following prerequisites installed:
* Java 
* JADE
The JADE graphical interface will be launched, showing the agents currently running.
### On Windows (launch.bat)
Run `launch.bat` to launch the application.
### On Linux (launch.sh)
Run `launch.sh` to launch the application.

### Examples 
To launch the application with different integral configurations, you can modify the parameters in the launch files. By default, they are configured as follows, and you have the flexibility to choose the function name as either "Sin" or "Logarithmic":

```bash
# Set agent names
set AGENT1="agent1"
set AGENT2="agent2"
set AGENT3="agent3"

# Set function name (choose either "Sin" or "Logarithmic")
set FUNCTION_NAME="Sin"

# Set function parameters
set MIN_VALUE=0
set MAX_VALUE=100
set DELTA_VALUE=0.1
```

## Project delivered
### Date
* december 25th 2023
### Deliverables
Archive containing :
* Project source code;
* Generated documentation;
* Launch file 

## Remarks
* Each agent (ComputeAgent) can be visualized in JADE's graphical user interface.
* Calculation results, execution times and process details are displayed in the console where the application is launched.

## Authors
* Ryma HADJI