package Server;

import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
    private String clientName = null;
    private boolean haveName = false;

    public ChatServerThread(ChatServer server, Socket socket){
        super();
        this.server = server;
        this.socket = socket;
        ID = socket.getPort();
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        }
        catch(IOException e){
        System.out.println(ID + " ERROR sending: " + e.getMessage());
        server.remove(ID);
        stop();
        }
    }

    public void run(){
        System.out.println("Server Thread " + ID + " running.");
        while(true){
            try{
            if(!haveName){
                setClientName(streamIn.readUTF());
                server.handle(ID,"//whoisonline");
                server.handle(ID,"//statusupdate");
                haveName = true;
                continue;
                }
            server.handle(ID, streamIn.readUTF());
            }
        catch(IOException e){
            System.out.println(ID + " ERROR reading: " + e.getMessage());
            server.handle(ID,"//statusupdatedisconnect");
            server.remove(ID);
            stop();
            }
        }
    }

    public void open() throws IOException{
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException{
        if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }

    public void setClientName(String clientName){
        this.clientName = clientName;
    }

    public String getClientName(){
        return clientName;
    }

    public int getID() {
        return ID;
    }

    public ChatServer getServer() {
        return server;
    }

    public void setServer(ChatServer server) {
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public DataInputStream getStreamIn() {
        return streamIn;
    }

    public void setStreamIn(DataInputStream streamIn) {
        this.streamIn = streamIn;
    }

    public DataOutputStream getStreamOut() {
        return streamOut;
    }

    public void setStreamOut(DataOutputStream streamOut) {
        this.streamOut = streamOut;
    }

    public boolean isHaveName() {
        return haveName;
    }

    public void setHaveName(boolean haveName) {
        this.haveName = haveName;
    }
}