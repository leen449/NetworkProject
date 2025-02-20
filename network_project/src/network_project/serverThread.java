package network_project;
import java.io.*; 
import java.net.*; 
import java.util.List;
public class serverThread implements Runnable {
    
private Socket socket ;
private PrintWriter out ;
private BufferedReader in;
private String playerName;

private List<serverThread>clients;
private static List<serverThread>waitingRoom;

public serverThread(Socket socket ,List<serverThread>clients , List<serverThread>waitingRoom ){
    this.socket = socket ;
    this.clients = clients ;
    this.waitingRoom = waitingRoom ;
    
}

    public void run() {
try{
    
 in= new BufferedReader(new InputStreamReader(socket.getInputStream()));   
 out = new PrintWriter(socket.getOutputStream());  
 
    

playerName=in.readLine();
if(playerName.isEmpty()){
out.println("iNVALID_NAME");   
socket.close();
return;
}

}catch(Exception a ){
    
}   
    
    
    }
  
    
}
