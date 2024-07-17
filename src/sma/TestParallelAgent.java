package sma;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This agent represents a coordinator for parallel computation using multiple agents.
 * It divides the computation task into intervals and distributes them among available agents.
 * The agent also performs local computation if no other agents are available.
 *
 * @author Ryma HADJI
 */
public class TestParallelAgent extends Agent {

    @Override
    protected void setup() {
        // Wait for a short period to allow other agents to start
        doWait(1000);
        // Read the arguments passed to the agent
        Object[] args = getArguments();
        String functName="";
		double min=0.0,max=0.0,delta=0.0;
        // Parse the arguments
        if((args != null) && (args.length==4)) {
            functName= args[0].toString();
		    min = Double.parseDouble(args[1].toString());
		    max= Double.parseDouble(args[2].toString());
		    delta  = Double.parseDouble(args[3].toString());
		}else {
			throw new IllegalArgumentException("Unexpected value: " + args.toString());
		}
        // Create a pointer to the Function
        Function function =  createFunction(functName, min, max, delta);
        // Search for agents providing the CALCULATION SERVICE
        List<AID> agents= searchAgentsFromService();
        // Perform local computation if no other agents are available
        if (agents.isEmpty()) {
            System.err.println("No agents found !! ");
            // Start time for local computation
            long start_time = System.currentTimeMillis();
            double resultLocal = function.eval();
            // End time for local computation
            long end_time = System.currentTimeMillis();
            long duration_time = end_time - start_time;
            // Display the local computation result
            System.out.println("Local computation processing ...");
            System.out.println("Calculation result : " + resultLocal);
            System.out.println("Calculation time : " + duration_time + " ms");
            return;
        }
        int nbAgent = agents.size();
        System.out.println("Number of agents found = " + nbAgent );
        // Divide the computation interval among agents
        DivideInterval interval= new DivideInterval(function.min, function.max);
        List<DivideInterval> division = interval.divide(agents.size());
        System.out.println("=================== START ======================\n");
        // Perform local computation
        System.out.println("------------  LOCAL COMPUTATION ----------------\n");
        localComputing(function, division);
        // Perform distributed computation
        System.out.println("------------ DISTRIBUTED COMPUTATION -----------\n");
        long start_time = System.currentTimeMillis();
        distributedComputing(agents, division, functName, delta);
        // Add a behavior to collect results from distributed computation
        addBehaviour(new SimpleBehaviour() {
            private double Sum = 0.0;
            private int counter = 0;
            @Override
            public void action() {
                ACLMessage msg= receive();
                if (msg != null) {
                    counter++;
                    double val = Double.parseDouble(msg.getContent());
                    Sum += val;
                } else {
                    block();
                }
            }
            @Override
            public boolean done() {
                return counter >= agents.size();
            }

            @Override
            public int onEnd() {
                long end_time = System.currentTimeMillis();
                long duration_time = end_time - start_time;
                System.out.println("Distributed computation processing ...");
                System.out.println("Computation result : " + Sum);
                System.out.println("Computation time : " + duration_time + " ms\n");
                return 0;
            }
        });
    }

    /**
     * Searches for agents providing the CALCULATION SERVICE and returns a list of their AIDs.
     *
     * @return List of AIDs of agents providing the CALCULATION SERVICE
     */
    private List<AID> searchAgentsFromService() {
        // Create a description for searching agents
        DFAgentDescription agentD = new DFAgentDescription();
        ServiceDescription serviceD = new ServiceDescription();
        serviceD.setType("CALCULATION SERVICE");
        agentD.addServices(serviceD);
        // List to store AIDs of agents providing the service
        List<AID> agents = new ArrayList<>();
        try {
            // Perform the search using DFService
            DFAgentDescription[] results = DFService.search(this, agentD);
            // Extract AIDs from the search results and add to the list
            for (int i = 0; i < results.length; i++) {
				agents.add(results[i].getName());
			}
            // Return the list of AIDs	
            return agents ;
        } catch (FIPAException e) {
            // Print stack trace if an exception occurs during the search
            e.printStackTrace();
            // Return an empty list in case of an exception
            return agents;
        }
    }

    /**
     * Performs local computation of the integral for the given function and divided intervals.
     *
     * @param function The function for which the integral is computed locally.
     * @param division List of divided intervals for parallel computation.
     */
    private void localComputing(Function function, List<DivideInterval> division) {
        // Record the start time of local computation
        long start_time = System.currentTimeMillis();
        // Perform local computation using streams to parallelize the computation
        double result = division.stream()
                .mapToDouble(r -> function.eval(r.getMin(), r.getMax()))
                .sum();
        // Record the end time of local computation
        long end_time = System.currentTimeMillis();
        // Calculate the duration of local computation
        long duration_time = end_time - start_time ; 
        // Print the local computation result and duration
        System.out.println("Local computation processing ...");
        System.out.println("Local calculation result : " + result);
        System.out.println("Local calculation time : " + duration_time + " ms\n");
    }
    /**
     * Initiates distributed computation by sending computation requests to multiple agents.
     *
     * @param agents List of agent AIDs to which computation requests are sent.
     * @param division List of divided intervals for parallel computation.
     * @param functName The name of the function to be computed.
     * @param delta The precision of the computation.
     */
    private void distributedComputing(List<AID> agents, List<DivideInterval> division, String functName, double delta) {
        for (int i = 0; i < agents.size() ; i++) {
            AID agentAid = agents.get(i);
            DivideInterval range = division.get(i);
            // Create a new ACLMessage for computation request
            ACLMessage msg= new ACLMessage(ACLMessage.REQUEST);
            // Set the content of the message with function details and range information
            msg.setContent(functName + "," + range.getMin() + "," + range.getMax() + "," +delta);
            // Add the receiver (agentAid) to the message
            msg.addReceiver(agentAid);
            // Send the computation request message to the agent
            send(msg);
        }
    }

    /**
     * Creates a new instance of a Function based on the provided parameters.
     *
     * @param functionName The name of the function.
     * @param min The minimum value of the interval for the function.
     * @param max The maximum value of the interval for the function.
     * @param delta The precision of the computation.
     * @return A new instance of the specified Function.
     * @throws IllegalArgumentException If the specified function name is not recognized.
     */
    private Function createFunction(String functionName, double min, double max, double delta) {
            switch (functionName.toUpperCase()) {
                case "SIN":
                    return new SinFunction(min, max, delta);
                case "LOGARITHMIC":
                    return new LogarithmicFunction(min, max, delta);
                default:
                    throw new IllegalArgumentException("The function " + functionName + " does not exist.");
            }
        }
    /**
     * A inner class representing a divided interval within a given range.
     * The class ensures that the minimum value of the interval is not greater than the maximum value.
     */
    private static class DivideInterval {
    private final double min;
    private final double max;
    /**
     * Constructs a DivideInterval with the specified minimum and maximum values.
     * Throws a RuntimeException if the minimum is greater than the maximum.
     *
     * @param min The minimum value of the interval.
     * @param max The maximum value of the interval.
     */
    public DivideInterval(double min, double max) {
        this.min = min;
        this.max = max;
        if (min > max) {
            throw new RuntimeException(String.format("Error : Interval minimum (%f) is greater than interval maximum (%f)", min, max));
            }
        }
        /**
         * Gets the minimum value of the interval.
         *
         * @return The minimum value of the interval.
         */
        public double getMin() {
            return min;
        }
        /**
         * Gets the maximum value of the interval.
         *
         * @return The maximum value of the interval.
         */
        public double getMax() {
            return max;
        }
        /**
         * Divides the interval into a specified number of divisions.
         *
         * @param nbDivisions The number of divisions to create within the interval.
         * @return A list of DivideInterval objects representing the divided intervals.
         */
        public List<DivideInterval> divide(int nbDivisions) {
            double size = max - min;
            double divisionSize = size / nbDivisions;
            List<DivideInterval> divisions = new ArrayList<>(nbDivisions);
       
            for (int i = 0; i < nbDivisions - 1; i++) {
                double minRange = min + (divisionSize * i);
                double maxRange = minRange + divisionSize;
                divisions.add(new DivideInterval(minRange, maxRange));
            }
    
            double _min = divisions.get(divisions.size() - 1).getMax();
            divisions.add(new DivideInterval(_min, max));

            return divisions;
        }
    }
}

