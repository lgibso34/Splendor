package splendor.common;

import splendor.common.util.Constants;
import splendor.common.util.Constants.ProtocolAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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
    private static Map<String, Integer> clientNames = new HashMap<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    private static Game game = new Game();

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
                name = receiveDisplayName(ProtocolAction.SetDisplayName, "valid");

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
                    //names.remove(name); Do not remove the name, so another user cannot pretend to be a previous user.
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                    System.out.println(e.getStackTrace());
                }
            }
        }

        String validateAction(String input) {
            StringBuilder sb = new StringBuilder(input.substring(0,1));
            ProtocolAction messageType = MessageType(input);
            String message = input.substring(1);

            switch (messageType) {
                case WithdrawCoins:
                    sb.append(validateWithdraw(message));
                    break;
                case BuyCard:
                    sb.append(validateBuy(message));
                    break;
                case ReserveCard:
                    sb.append(validateReserve(message));
                    break;
                case DepositCoins:
                    sb.append(validateDeposit(message));
                    break;
                case AcquireNoble:
                    sb.append(validateNoble(message));
                    break;
                case SyncTime:
                    sb.append(syncTime(message));
                    break;
                case SyncGameState:
                    sb.append(syncState(message));
                    break;
                case SyncChat:
                    sb.append(syncChat(message));
                    break;
                case SendChat:
                    sb.append(sendChat(message));
                    break;
                case SetDisplayName:
                    //this could be used to nickname people
                    sb.append(setDisplayName(message));
                    break;
                case CheckAlive:
                    sb.append(checkAlive(message));
                    break;
                case Acknowledge:
                    sb.append(acknowledge(message));
                    break;
                default:
                    sb.append(sendResendMessage(message));
                    break;
            }
            return sb.toString();
        }

        String validateWithdraw(String message) {
            // TODO: validate game logic
            //TODO game has all of the state and validation. delegate the message to game with the player number to validate against the game state
            //boolean valid = game.validateWithdraw(coins, getPlayerNumber(name);
            boolean valid = false;
            if(!valid)
                return null;
            return "valid";
        }

        String validateBuy(String message) {
            // TODO: validate game logic
            return null;
        }

        String validateReserve(String message) {
            //TODO: validate game logic
            return null;
        }

        String validateDeposit(String message) {
            //TODO: validate game logic
            return null;
        }

        String validateNoble(String message) {
            //TODO: validate game logic
            return null;
        }

        String syncTime(String message) {
            //TODO: validate game logic
            return null;
        }

        String syncState(String message) {
            //TODO: validate game logic
            return null;
        }

        String syncChat(String message) {
            //TODO: validate game logic
            return null;
        }

        String sendChat(String message) {
            //TODO: validate game logic
            return null;
        }

        String setDisplayName(String message) {
            //TODO: validate game logic
            return null;
        }

        String checkAlive(String message) {
            //TODO: validate game logic
            return null;
        }

        String acknowledge(String message) {
            //TODO: validate game logic
            return null;
        }

        String sendResendMessage(String message) {
            //TODO: validate game logic
            return null;
        }

        private boolean receiveAndSend(ProtocolAction messageType, String send) {
            String response = in.nextLine();
            if (MessageType(response) == messageType)
                return true;
            return false;
        }

        private String receiveDisplayName(ProtocolAction messageType, String send) {
            String response;
            while (true) {
                response = in.nextLine();
                if (MessageType(response) == messageType) {
                    boolean player = response.charAt(1) == '1' ? true : false;
                    response = response.substring(2);
                    if (response != null) {
                        synchronized (clientNames) {
                            if (!response.isBlank() && !clientNames.containsKey(response)) {
                                clientNames.put(response, game.addClient(player));
                                System.out.println(new StringBuilder("Added ").append(response).append(" as a ").append(clientNames.get(response) >= 0 ? "player." : "spectator.").toString());
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

        private ProtocolAction MessageType(String s){
            return ProtocolAction.fromInt(Integer.parseInt(s.substring(0,1)));
        }
    }
}