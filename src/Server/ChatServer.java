package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

    //Max user limit is capped at 50. You can use array list and remove this cap.
    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;


    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        }
        catch(IOException e)
        {
             System.out.println("Can not bind to port " + port + ": " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        if (args.length != 1){
            System.out.println("Usage: -java jarname(ex: AntChatv1)  port(1000-99999)");
            return;
        }
        else if(Integer.parseInt(args[0]) < 1000 || Integer.parseInt(args[0]) > 99999){
            System.out.println("Port must be 4 or 5 digits!");
            return;
        }
            new ChatServer(Integer.parseInt(args[0]));

    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                 addThread(server.accept());
            }
            catch(IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start(){
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop(){
        if (thread != null){
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
        if (clients[i].getID() == ID){
            return i; }
        return -1;
    }

    public synchronized void handle(int ID, String input) {
        switch (input) {
            case "//typing":
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getID() == ID) {
                        continue;
                    }
                    clients[i].send("//info-" + clients[findClient(ID)].getClientName() + " is typing...");
                }
                break;
            case "//whoisonline":
                String message = "";
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getID() == ID) { //??????????????????? neden?????????
                        continue;
                    }
                    message = message + "-" + clients[i].getClientName();
                }

                clients[findClient(ID)].send("//whoisonline" + message);
                break;
            case "//statusupdate":
                for (int i = 0; i < clientCount; i++) {
                    clients[i].send("//status-" + clients[findClient(ID)].getClientName());
                }
                break;
            case "//statusupdatedisconnect":
                int who = 0; //Who represent the person on the online status list queue.
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getID() == ID) {
                        who = i + 1; //Array starts from 0 so we add 1 to match the online list.
                    }
                }
                for (int i = 0; i < clientCount; i++) {
                    clients[i].send("//statusdisconnect-" + who);
                }
                break;
            default:
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getID() == ID) {//This prevents the person's own message to show up again.
                        continue;
                    }
                    clients[i].send(input);
                }
                break;
        }
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount-1)
                for (int i = pos+1; i < clientCount; i++)
                    clients[i-1] = clients[i];
            clientCount--;
            try {
                toTerminate.close();
            }
            catch(IOException ioe) {
                System.out.println("Error closing thread: " + ioe); }
                toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket);
        try {
            clients[clientCount].open();
            clients[clientCount].start();
            clientCount++;
            }
        catch(IOException e)
            {
                System.out.println("Error opening thread: " + e);
            }

        }
        else{
        System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }


    public ChatServerThread[] getClients() {
        return clients;
    }

    public void setClients(ChatServerThread[] clients) {
        this.clients = clients;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }
}