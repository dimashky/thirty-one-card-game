package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerMain {

    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService clientsThreadPool;
    private Socket clientSocket;
    
    public ServerMain(int port) {
        this.port = port;
        this.clientsThreadPool = Executors.newFixedThreadPool(4);
    }
    
    
    public static void main(String[] args) {
        ServerMain mainServer = new ServerMain(5002);
        try {
            mainServer.startServer();
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void startServer() throws IOException{
        serverSocket = new ServerSocket(port);
        System.out.println("Start Server!!!");
        while (true) {
            clientSocket = serverSocket.accept();
            ClientHandler runnable = new ClientHandler(clientSocket);
            clientsThreadPool.execute(runnable);
        }
    }
}

