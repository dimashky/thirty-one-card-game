package server;

import gui.BoardGame;
import java.net.ServerSocket;

import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

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
            System.out.println(msg);
            String[] msgs = msg.split("_");
            switch(msgs[0]){
                case "CARD":
                    Game.getInstance().update(Integer.parseInt(msgs[1]), false);
                    break;
                    
                case "REMATCH":
                    int confirm = JOptionPane.showConfirmDialog(null, "Rematch ?!!");
                    if(confirm == 0){
                        Game.getInstance().send("REMATCHACCEPT_");
                        BoardGame.getInstance().rematch(true);
                    }
                    break;
                    
                case "REMATCHACCEPT":                    
                    BoardGame.getInstance().rematch(false);
                    break;
                    
                case "SURRENDER":
                    BoardGame.getInstance().surrender(false);
                    break;
                    
                case "GAMEOVER":
                    Game.getInstance().finishTheGame(Integer.parseInt(msgs[1]));
                    break;
                    
                case "CLOSEWINDOW" :
                    disconnect = true;
                    System.out.println("Disconnected");
                    BoardGame.getInstance().disconnected();
                    return;
                    
                default:
                    System.out.println("DEFAULT !!");
            }
            
        }

    }

}
