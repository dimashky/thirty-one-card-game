package server;

import java.net.ServerSocket;

import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerMain {

    public static boolean disconnect = false;

    
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

        new Game(client);
        
        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());

        while (!disconnect) {
            String msg = dataInputStream.readUTF();
            String[] msgs = msg.split("_");
            switch(msgs[0]){
                case "CARD":
                    Game.getInstance().update(Integer.parseInt(msgs[1]), false);
                    break;
                case "REMATCH":
                    
                    break;
                case "SURRENDER":
                    Game.getInstance().rivalSurrender();
                    break;
                case "GAMEOVER":
                    Game.getInstance().finishTheGame(Integer.parseInt(msgs[1]));
                    break;
                case "CLOSEWINDOW" :
                    disconnect = true;
                    System.out.println("Disconnected");
                    //TODO: Show Disconnected not surrender
                    Game.getInstance().rivalSurrender();
                    return;
                default:
                    System.out.println("kdlkfdlk");
            }
            
        }

    }

}
