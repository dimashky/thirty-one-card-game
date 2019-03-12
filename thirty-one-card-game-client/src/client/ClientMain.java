package client;

import java.net.Socket;
import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientMain {
	
    public static boolean disconnect = false;
	
    public static void main(String[] args) throws IOException {
        System.out.println("Hello Client");
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("127.0.0.1", 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Game(clientSocket);

        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
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
                    //closeStreams(clientSocket, dataInputStream);
                    //Game.getInstance().closeConnection();
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



