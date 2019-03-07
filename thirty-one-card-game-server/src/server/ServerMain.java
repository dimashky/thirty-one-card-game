package server;

import java.net.ServerSocket;

import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Server");
        ServerSocket socket = null;
        Socket client = null;
        try {
            socket = new ServerSocket(5000);
            client = socket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ServerRun runnable = new ServerRun(client);
        Thread t = new Thread(runnable);
        t.start();
        new Game(client);

    }

}

class ServerRun implements Runnable {

    Socket clientSocket = null;
    DataInputStream dataInputStream = null;

    public ServerRun(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Game.getInstance().update(dataInputStream.readInt(), false);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
