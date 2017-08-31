package Client.Utility;

import Client.ApplicationContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StreamConfiguration {
    private Socket socket;
    private String hostAddr;
    private int portAddr = 0;
    private String name;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;



    public void initSocket() throws IOException {
        if(hostAddr.isEmpty() || portAddr == 0){
            ApplicationContext.getStageConfiguration().showErrorStage("Invalid host address or post address. Please try again...");
            return;
        }
        this.socket = new Socket(hostAddr,portAddr);
    }

    public void initStream() throws IOException{
        try{
            streamIn = new DataInputStream(socket.getInputStream());
            streamOut = new DataOutputStream(socket.getOutputStream());
            streamOut.writeUTF(name);
            streamOut.flush();
        }
        catch(IOException e) {
            ApplicationContext.getStageConfiguration().showErrorStage("An error occurred while initializing streams. Please check the server and try again...");
            System.exit(0);
        }

    }

    public void stopStream(){
        try{
            if(streamIn != null) streamIn.close();
            if(streamOut != null) streamOut.close();
            if(socket != null) socket.close();
        }catch(IOException e){
            ApplicationContext.getStageConfiguration().showErrorStage("An error occurred closing the application. Shutdown initiated.");
            System.exit(0);
        }

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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getHostAddr() {
        return hostAddr;
    }

    public void setHostAddr(String hostAddr) {
        this.hostAddr = hostAddr;
    }

    public int getPortAddr() {
        return portAddr;
    }

    public void setPortAddr(int portAddr) {
        this.portAddr = portAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
