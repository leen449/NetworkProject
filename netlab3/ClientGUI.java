
package netlab3;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextArea playerListArea, waitingListArea;
    private JButton connectButton, joinGameButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private JFrame waitingRoomFrame;

    public ClientGUI() {
        frame = new JFrame("Game Lobby");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Name Input Panel
        JPanel namePanel = new JPanel();
        nameField = new JTextField(15);
        connectButton = new JButton("Connect");
        namePanel.add(new JLabel("Enter Name:"));
        namePanel.add(nameField);
        namePanel.add(connectButton);

        // Player List Panel (Lobby)
        playerListArea = new JTextArea();
        playerListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playerListArea);

        // Join Game Button
        joinGameButton = new JButton("Join Game");
        joinGameButton.setEnabled(false);

        frame.add(namePanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(joinGameButton, BorderLayout.SOUTH);

        connectButton.addActionListener(e -> connectToServer());
        joinGameButton.addActionListener(e -> joinGame());

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 9090);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            playerName = nameField.getText();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name.");
                return;
            }

            out.println(playerName);
            connectButton.setEnabled(false);
            joinGameButton.setEnabled(true);

            new Thread(this::listenToServer).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to server.");
        }
    }

    private void listenToServer() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("PLAYER_LIST:")) {
                    updatePlayerList(message.substring(12));
                } else if (message.startsWith("WAITING_LIST:")) {
                    updateWaitingList(message.substring(13));
                } else if (message.equals("GAME_START")) {
                    JOptionPane.showMessageDialog(waitingRoomFrame, "Game is starting!");
                    waitingRoomFrame.dispose();
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Connection lost.");
        }
    }

    private void updatePlayerList(String players) {
        SwingUtilities.invokeLater(() -> {
            playerListArea.setText("Connected Players:\n" + players.replace(",", "\n"));
        });
    }

    private void updateWaitingList(String players) {
        SwingUtilities.invokeLater(() -> {
            waitingListArea.setText("Waiting Room Players:\n" + players.replace(",", "\n"));
        });
    }
    
    private void joinGame() {
        out.println("JOIN_GAME");
        frame.dispose();
        showWaitingRoom();
    }
    
     private void showWaitingRoom() {
        waitingRoomFrame = new JFrame("Waiting Room");
        waitingRoomFrame.setSize(300, 200);
        waitingListArea = new JTextArea();
        waitingListArea.setEditable(false);
        waitingRoomFrame.add(new JScrollPane(waitingListArea), BorderLayout.CENTER);
        waitingRoomFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
    
}



