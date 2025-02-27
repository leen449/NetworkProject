package netlab3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NewServer {

    private static List<NewClient> clients = new ArrayList<>();
    private static List<NewClient> waitingRoom = new ArrayList<>();
    private static boolean timerStarted = false;

    public static void main(String[] args) {
        try {
            String hostIP = InetAddress.getLocalHost().getHostAddress();
            ServerSocket serverSocket = new ServerSocket(9090, 50, InetAddress.getByName(hostIP));

            System.out.println("Server is running on IP: " + hostIP + " and port 9090");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                NewClient clientThread = new NewClient(clientSocket, clients);
                clients.add(clientThread);
                new Thread(clientThread).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to start the server.");
        }
    }

    public static synchronized void updatePlayerList() {
        StringBuilder playerList = new StringBuilder("PLAYER_LIST:");
        for (NewClient client : clients) {
            playerList.append(client.getName()).append(",");
        }

        for (NewClient client : clients) {
            client.send(playerList.toString());
        }
    }

    public static synchronized void removeClient(NewClient client) {
        clients.remove(client);
        waitingRoom.remove(client);
        updatePlayerList();
        System.out.println(client.getName() + " has left the game.");
    }

    public static synchronized void addToWaitingRoom(NewClient client) {
        waitingRoom.add(client);
        updateWaitingRoomList();

        System.out.println(client.getName() + " joined the waiting room! Total: " + waitingRoom.size());

        if (waitingRoom.size() == 2 && !timerStarted) {
            timerStarted = true;
            startCountdown();
        }

        if (waitingRoom.size() == 4) {
            startGame();
        }
    }

    public static synchronized void updateWaitingRoomList() {
        StringBuilder waitingList = new StringBuilder("WAITING_LIST:");
        for (NewClient client : waitingRoom) {
            waitingList.append(client.getName()).append(",");
        }

        for (NewClient client : waitingRoom) {
            client.send(waitingList.toString());
        }
    }

    private static void startCountdown() {
        new Thread(() -> {
            try {
                System.out.println("Game will start in 30 seconds...");
                Thread.sleep(30000);
                if (waitingRoom.size() >= 2) {
                    startGame();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void startGame() {
        System.out.println("Game is starting with " + waitingRoom.size() + " players!");
        for (NewClient client : waitingRoom) {
            client.send("GAME_START");
        }
        waitingRoom.clear();
        timerStarted = false;
    }
}
