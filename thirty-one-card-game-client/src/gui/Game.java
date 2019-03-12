package gui;

import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Game {

    private static Game instance;
    public BoardGame board;
    private static Socket client;
    DataOutputStream dos;

    static public Game getInstance() throws IOException {
        if (instance == null) {
            instance = new Game(client);
        }
        return instance;
    }

    public Game(Socket client) throws IOException {
        Game.client = client;
        dos = new DataOutputStream(client.getOutputStream());
        initGame(true);
    }

    public void send(String msg) throws IOException {
        dos.writeUTF(msg);
    }

    public void update(int cardNumber, boolean turn) {
        BoardGame.getInstance().update(cardNumber, turn);
    }

    public void finishTheGame(int cardNumber){
        BoardGame.getInstance().finishTheGame(cardNumber);
    }
    
    public void rivalSurrender() {
        BoardGame.getInstance().rivalSurrender();
    }
    
    public void initGame(boolean turn) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    board = BoardGame.getInstance();
                    board.setVisible(true);
                    //board.setTurn(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
