package client;

import gui.BoardGame;
import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import rmi.ScoresInterface;

public class ClientMain {

    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        Registry registery = LocateRegistry.getRegistry(123);
        ScoresInterface sc = (ScoresInterface) registery.lookup("getScore");
        ScoresInterface sc2 = (ScoresInterface) registery.lookup("updateScore");
        
        Socket clientSocket = null;
        String username = "";

        while (username == "" || username == null || username.length() == 0) {
            username = JOptionPane.showInputDialog("Please insert your name");
        }

        try {
            clientSocket = new Socket("127.0.0.1", 5002);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game g = new Game(clientSocket, username);

        g.send("USERNAME_" + username);

        System.out.println("Hello Client: " + username);
        System.out.println("Your Score: " + sc.getScore(username));

        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

        while (true) {
            String msg = dataInputStream.readUTF();
            String[] msgs = msg.split("_");
            System.out.println("INCOMING Msg FROM SERVER => " + msg);
            switch (msgs[0]) {
                case "CARD":
                    boolean turn = !msgs[3].equals(username);
                    boolean isGameOver = g.update(Integer.parseInt(msgs[1]), !msgs[2].equals(username),
                            msgs[2], msgs[3].equals(username));

                    if (isGameOver) {
                        JOptionPane.showMessageDialog(null, "Game Over");
                        if(turn){
                            sc2.updateScore(username);
                        }
                    }

                    break;
                    
                case "NOTIFICATION":
                    
                     BoardGame.getInstance().setTextArea(msgs[1]);
                    break;
               
                case "GAMEOVER":
                    BoardGame.getInstance().rivalSurrender();
                    return;
                case "START":
                    BoardGame.getInstance().setTurn(!msgs[1].equals(username),msgs[1]);
                    break;
                case "CARDS":
                    int[] cards = new int[6];
                    for (int i = 1; i <= 6; ++i) {
                        cards[i - 1] = Integer.parseInt(msgs[i]);
                    }
                    BoardGame.getInstance().initCards(cards);
                    break;
                default:
                    System.out.println("DEFAULT OPTION HERE? WHAT?!");
            }
        }

    }

}