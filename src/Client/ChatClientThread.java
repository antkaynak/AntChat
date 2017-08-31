package Client;

import Client.Controllers.ClientGUI;


import java.io.DataInputStream;
import java.net.SocketException;

public class ChatClientThread extends Thread
{

    private ClientGUI client;
    private DataInputStream  streamIn;

    public ChatClientThread(ClientGUI client)
    {
        this.client   = client;
        streamIn  = ApplicationContext.getStreamConfiguration().getStreamIn();
        start();
    }


    public void run(){
        while (true){
        try {
            //Wait for data from stream and when data is accessed send it to ClientGUI handle method to process it.
            client.handle(streamIn.readUTF());
        }
        catch(SocketException se){
            if(Thread.activeCount() > 1){
                client.handle("An error occurred. Please restart the client...");
                break;
            }
            se.printStackTrace();
        }
        catch(Exception e){
               ApplicationContext.getStreamConfiguration().stopStream();
               e.printStackTrace();
               break;
           }
       }
    }
}
