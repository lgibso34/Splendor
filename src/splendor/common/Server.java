package splendor.common;

import splendor.common.util.Constants;
import splendor.common.util.Constants.ProtocolAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * A multithreaded chat room server. When a client connects the server requests a screen
 * name by sending the client the text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received. After a client submits a unique name, the server acknowledges
 * with "NAMEACCEPTED". Then all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name. The broadcast messages are prefixed
 * with "MESSAGE".
 * <p>
 * This is just a teaching example so it can be enhanced in many ways, e.g., better
 * logging. Another is to accept a lot of fun commands, like Slack.
 */
public class Server {

    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The Splendor server is running...");
        var pool = Executors.newFixedThreadPool(Constants.MAX_CLIENTS);
        try (var listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a screen name until a
         * unique one has been submitted, then acknowledges the name and registers the
         * output stream for the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                // Keep requesting a name until we get a unique one.
                //while (true) {
                //out.println("SUBMITNAME");
                name = receiveDisplayName(ProtocolAction.SetDisplayName, "valid");
                //name = in.nextLine();
//                if (name == null) {
//                    return;
//                }
//                synchronized (names) {
//                    if (!name.isBlank() && !names.contains(name)) {
//                        names.add(name);
//                        break;
//                    }
//                }
                //}

                // Now that a successful name has been chosen, add the socket's print writer
                // to the set of all writers so this client can receive broadcast messages.
                // But BEFORE THAT, let everyone else know that the new person has joined!
                System.out.println(name + " has joined");
                //out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);

                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    String message = "";
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    } else {
                        // this is what actually fires when a client sends a message to the server
                        message = validateAction(input);
                    }
                    if (message != "") {
                        for (PrintWriter writer : writers) {
                            writer.println(message);
                        }
                    }
                }
            } catch (Exception e) {
                if (e instanceof NoSuchElementException)
                    System.out.println("Socket Closed for client: " + name);
                else {
                    System.out.println(e);
                    System.out.println(e.getStackTrace());
                }
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        String validateAction(String input) {
            String msg = "";
            if (input.startsWith("WITHDRAW")) msg = validateWithdraw(input);
            else if (input.startsWith("BUY")) msg = validateBuy(input);
            else if (input.startsWith("RESERVE")) msg = validateReserve(input);
                // ... additional else-ifs to cover all input actions
            else msg = "REJECT";
            return msg;
        }

        String validateWithdraw(String input) {
            String coins = input.substring(8);
            // TODO: validate game logic
            String retVal = "WITHDRAW " + name + " " + coins;
            return retVal;
        }

        String validateBuy(String input) {
            String card = input.substring(4);
            // TODO: validate game logic
            String retVal = "BUY " + name + " " + card;
            return retVal;
        }

        String validateReserve(String input) {
            String card = input.substring(7);
            //TODO: validate game logic
            String retVal = "RESERVE " + name + " " + card;
            return retVal;
        }

        private boolean receiveAndSend(ProtocolAction messageType, String send) {
            String response = in.nextLine();
            if (Integer.parseInt(response.substring(0, 1)) == messageType.ordinal())
                return true;
            return false;
        }

        private String receiveDisplayName(ProtocolAction messageType, String send) {
            String response;
            while (true) {
                response = in.nextLine();
                if (Integer.parseInt(response.substring(0, 1)) == messageType.ordinal()) {
                    response = response.substring(1);
                    if (response != null) {
                        synchronized (names) {
                            if (!response.isBlank() && !names.contains(response)) {
                                names.add(response);
                                break;
                            }
                        }
                    }
                }
            }
            System.out.println("Sending: " + messageType.ordinal() + "valid");
            out.println(messageType.ordinal() + "valid");
            return response;
        }
    }
}