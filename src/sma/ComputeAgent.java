package sma;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
/**
 * Represents an agent responsible for performing calculations.
 * The agent registers itself as a service and waits for messages containing computation requests.
 * Upon receiving a request, the agent creates the specified mathematical function and computes the result,
 * then sends an informative reply with the result.
 * 
 * @author Ryma HADJI
 */

public class ComputeAgent extends Agent {
    /** The type of service provided by the agent. */
    private static final String SERVICE_TYPE = "CALCULATION SERVICE";
    /** The mathematical function to be computed. */
    protected Function function;
    /**
     * Setup method for initializing the agent.
     * Registers the agent as a service and adds a behavior to handle computation requests.
     */
    protected void setup() {
        registerAgentAsService();
        addBehaviour(new CyclicBehaviour() {

            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    // récuperer le contenu du message
                    String[] args = parseMessageContentToArguments(msg.getContent());
                    function = createFunction(args);
                    double result = function.eval();
                    sendInformativeReply(msg, result);
                    System.out.println("The agent: " + getLocalName() + " sent a reply:\n" +
                    "Min = " + Double.parseDouble(args[1]) + " and Max = " + Double.parseDouble(args[2]) + "\n" +
                    "Result = " + result);

                } else block();
            }
        });
    }
    /**
     * Sends an informative reply with the computed result.
     * 
     * @param msg The received message containing the computation request.
     * @param val The computed result.
     */
    private void sendInformativeReply(ACLMessage msg, double val) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent(String.valueOf(val));
        send(reply);
    }
    /**
     * Parses the content of the message into an array of arguments.
     * 
     * @param msgContent The content of the received message.
     * @return An array of arguments parsed from the message content.
     * @throws IllegalArgumentException if the format of the arguments is invalid.
     */
    private String[] parseMessageContentToArguments(String msgContent) {
        String[] args = msgContent.split(",");
        // Vérifier que chaque élément peut être converti en double
        for (int i = 1; i < args.length; i++) {
            try {
                Double.parseDouble(args[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format for argument at index " + i, e);
            }
        }
        // Convertir les éléments en double puis en String
        for (int i = 1; i < args.length; i++) {
            args[i] = Double.toString(Double.parseDouble(args[i]));
        }
        
        return args;
    }
    /**
     * Registers the agent as a service of type "CALCULATION SERVICE".
     */
    private void registerAgentAsService() {
        System.out.println("The agent : "+getLocalName()+" is registered as a service of type "+SERVICE_TYPE+"\n");
        ServiceDescription serviceD = new ServiceDescription();
        serviceD.setType(SERVICE_TYPE);
        serviceD.setName(getLocalName());
        register(serviceD);
    }
    
    /**
     * Creates a mathematical function based on the specified arguments.
     * 
     * @param args The arguments specifying the type and parameters of the function.
     * @return The created mathematical function.
     * @throws IllegalArgumentException if the specified function type does not exist.
     */
    private Function createFunction(String[] args) {
        switch (args[0].toUpperCase()) {
            case "SIN":
                return new SinFunction(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            case "LOGARITHMIC":
                return new LogarithmicFunction(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            default:
                throw new IllegalArgumentException("Error : The function does not exist." );
        }
    }
    /**
     * Registers the agent to the specified service.
     * 
     * @param serviceD The service description to be registered.
     */
    private void register(ServiceDescription serviceD) {
        DFAgentDescription dfAgentD = new DFAgentDescription();
        dfAgentD.setName(getAID());
        dfAgentD.addServices(serviceD);
        try {
            DFService.register(this, dfAgentD);
        } catch (FIPAException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /**
     * Method called when the agent is terminated.
     * Deregisters the agent from the service.
     */
    protected void takeDown() {
        System.out.println("Tha agent : "+getLocalName()+" is deregistered from the "+SERVICE_TYPE+"\n");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

 
    

    

    


    
}
