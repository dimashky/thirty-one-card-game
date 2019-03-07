package client;

import java.net.Socket;
import gui.Game;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientMain {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Hello Client");
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
                ClientRun runnable = new ClientRun(socket);
        		new Game(socket);
                Thread t = new Thread(runnable);
                t.start();
	}
	
}

class ClientRun implements Runnable {

    Socket clientSocket = null;
    DataInputStream dataInputStream = null;
    
    public ClientRun(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            while(true){
            	String msg = dataInputStream.readUTF();
            	String[] msgs = msg.split("_");
            	System.out.println(msgs[0] + " " + msgs[1]);
            	int cardNumber = Integer.parseInt(msgs[1]);
                Game.getInstance().update(cardNumber, false);
            }
        } catch (IOException ex) {
                Logger.getLogger(ClientRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
