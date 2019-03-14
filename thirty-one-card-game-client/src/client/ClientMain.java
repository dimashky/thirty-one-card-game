package client;

import gui.BoardGame;
import java.net.Socket;
import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


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
            System.out.println(msg);
            String[] msgs = msg.split("_");
            switch(msgs[0]){
                case "CARD":
                    // false == myTurn 
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
                    System.out.println("DISCONNECTED");
                    BoardGame.getInstance().disconnected();
                    return;
                    
                default:
                    System.out.println("DEFAULT !!");
            }
        }

    }

	
}



