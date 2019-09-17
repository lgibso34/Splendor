package splendor.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import splendor.common.util.Constants;
import splendor.common.util.Constants.ProtocolAction;
import splendor.common.util.DH_AES;
import splendor.common.util.ProtocolMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// TO RUN: make sure you specify the server address in the terminal:
// javac splendorClient.java && java splendorClient localhost

/**
 * A simple Swing-based client for the chat server. Graphically it is a frame with a text
 * field for entering messages and a textarea to see the whole dialog.
 * <p>
 * The client follows the following Chat Protocol. When the server sends "SUBMITNAME" the
 * client replies with the desired screen name. The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are already in use. When the
 * server sends a line beginning with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all chatters connected to the
 * server. When the server sends a line beginning with "MESSAGE" then all characters
 * following this string should be displayed in its message area.
 */
public class Client extends Application {

    String serverAddress;
    int playerID = 0;
    private static final int MAX_PLAYERS = 5;
    private DH_AES clientDH = new DH_AES();

    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Player");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);

    /**
     * Constructs the client by laying out the GUI and registering a listener with the
     * textfield so that pressing Return in the listener sends the textfield contents
     * to the server. Note however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED message from
     * the server.
     */
    public Client() { //String serverAddress
//        this.serverAddress = serverAddress;
//
//        textField.setEditable(false);
//        messageArea.setEditable(false);
//        frame.getContentPane().add(textField, BorderLayout.SOUTH);
//        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
//        frame.pack();
//
//        // Send on enter then clear to prepare for next message
//        textField.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                out.println(textField.getText());
//                textField.setText("");
//            }
//        });
    }

    // Starting at 1, increment playerID upwards as new players are added
    // All players above 4 will be rejected
    //TODO move this to server side
    private int addPlayer() {
        if (playerID >= Constants.MAX_CLIENTS)
            terminate();
        return ++playerID;
    }

    void terminate() {
//        System.out.println("Terminating!");
//        frame.setVisible(false);
//        frame.dispose();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Pass the server IP as the sole command line argument");
//            return;
//        }
//        var client = new Client(args[0]);
//        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        client.frame.setVisible(true);
//        client.run();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.serverAddress = "localhost";
        var socket = new Socket(serverAddress, 59001);

        ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
        //oOut.writeObject(obj);
        ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());
        //ProtocolMessage message = (ProtocolMessage)oIn.readObject();

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        //setup encryption
        setupDH(oIn, oOut);

//        while (in.hasNextLine()) {
//            var line = in.nextLine();
//            if (line.startsWith("SUBMITNAME")) {
//                out.println(addPlayer());
//            } else if (line.startsWith("NAMEACCEPTED")) {
//                this.frame.setTitle("Player " + line.substring(13));
//                textField.setEditable(true);
//            } else if (line.startsWith("REJECT")) {
//                messageArea.append("Invalid move was rejected\n");
//            } else {
//                // directly echo the server response into the chat area
//                messageArea.append(line + "\n");
//            }
//        }

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("New Chat User");
        Label label1 = new Label("Please enter a display name:");
        TextField text1 = new TextField();
        text1.setPrefWidth(200);
        text1.setMaxWidth(200);

        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("Player");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("Spectator");
        rb2.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {

                }
            }
        });

        Button button1 = new Button("Register");
        button1.setOnAction(e -> {
            if (sendAndReceive(ProtocolAction.SetDisplayName, text1.getText(), rb1.isSelected())) {
                popupWindow.close();
            } else
                label1.setText(text1.getText() + " is invalid. Please try another name.");
        });

        HBox hLayout = new HBox(10);
        hLayout.setAlignment(Pos.CENTER);
        hLayout.getChildren().addAll(rb1, rb2);

        VBox vLayout = new VBox(10);
        vLayout.getChildren().addAll(label1, text1, hLayout, button1);
        vLayout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(vLayout, 300, 250);
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();

        StackPane stackPane = new StackPane();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stackPane.getChildren().add(root);

        primaryStage.setTitle("Hello World");


        Rectangle rect = new Rectangle(200, 200, Color.RED);
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(120, 120);
        s1.setContent(rect);

        stackPane.getChildren().add(s1);

        primaryStage.setScene(new Scene(stackPane, 300, 275));
        primaryStage.show();
    }

    private boolean sendAndReceive(ProtocolAction messageType, String message, boolean player) {
        out.println(messageType.ordinal() + (player ? "1" : "0") + message);
        String response = in.nextLine();
        System.out.println("received: " + response);//debug
        if(response.equals(messageType.ordinal() + "valid"))
            return true;
        return false;
    }

    private void setupDH(ObjectInputStream oIn, ObjectOutputStream oOut) {
        while (true) {
            try {
                ProtocolMessage pm = (ProtocolMessage) oIn.readObject();
                if (pm.getMessageType() == ProtocolAction.KeyExchange) {
                    Object message = pm.getMessage();
                    if (message != null) {
                        clientDH.setReceiverPublicKey((PublicKey)message);
                        oOut.writeObject(new ProtocolMessage(ProtocolAction.KeyExchange, clientDH.getPublicKey()));
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error setting up DH.");
            }
        }
    }

}