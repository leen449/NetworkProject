package network_project;
import java.io.*; 
import java.net.*; 

public class gameServer {

    public static void main(String[] args) throws Exception{
         ServerSocket serverSocket = new ServerSocket(2756); 
         
         while(true){
         try{
         
         Socket clientSocket = serverSocket.accept();
         }catch(Exception a ){
         
         
         }
         
         
         
         }
    
    }
    
}
