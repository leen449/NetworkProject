package netlab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

class NewClient implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private List<NewClient> clients;

    public NewClient(Socket socket, List<NewClient> clients) throws IOException {
        this.client = socket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    public String getName() {
        return name;
    }

    public void run() {
    try {
        name = in.readLine();
        System.out.println(name + " has connected.");
        NewServer.updatePlayerList();

        while (true) {
            String message = in.readLine();
            if (message == null) break;
            
            if (message.equals("JOIN_GAME")) {
                NewServer.addToWaitingRoom(this);
            } else if (message.equals("LEAVE_WAITING_ROOM")) {
                NewServer.removeFromWaitingRoom(this);
            }
        }
    } catch (IOException e) {
        System.out.println(name + " disconnected.");
    } finally {
        closeConnections();
}
}

    public void send(String message) {
        out.println(message);
    }

    private void closeConnections() {
        try {
            NewServer.removeClient(this);
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
