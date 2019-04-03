package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javafx.util.Pair;

public class ClientHandler implements Runnable {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket clientSocket;
    public static ArrayList<Pair<String, ClientHandler>> clients = new ArrayList<>();
    public static int[] cards = new int[] {4, 4, 4, 4, 4, 4};
    public static String currUser = "";
    
    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    static synchronized void broadcast(String msg) throws IOException {
        for (Pair<String, ClientHandler> client : ClientHandler.clients) {
            client.getValue().dataOutputStream.writeUTF(msg);
        }
    }

    private int getClientIdx() {
        for (int i = 0; i < ClientHandler.clients.size(); ++i) {
            if (ClientHandler.clients.get(i).getValue() == this) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = dataInputStream.readUTF();
                System.out.println("Msg to SERVER: " + msg);
                String[] msgs = msg.split("_");
                int idx = 0;
                String nextUser;
                switch (msgs[0]) {
                    case "USERNAME":
                        ClientHandler.clients.add(new Pair(msgs[1], this));

                        System.out.println("USERNAME "+ msgs[1]);

                        if(ClientHandler.clients.size() == 2){
                            ClientHandler.broadcast("START_"+ClientHandler.clients.get(0).getKey());
                            System.out.println("BROADCAST start "+ ClientHandler.clients.get(0).getKey() 
                                    + " will start.");
                        }
                        
                        ClientHandler.broadcast("NOTIFICATION_NEW Player "+msgs[1] +"\n");
                        
                        String remain = "CARDS";
                        
                        for(int i : cards){
                            remain += "_" + i;
                        }
                        
                        this.dataOutputStream.writeUTF(remain);
                        
                        break;
                        
                    case "CARD":
                        cards[Integer.parseInt(msgs[1])-1]--;
                        idx = getClientIdx();
                        currUser = ClientHandler.clients.get(idx).getKey();
                        nextUser = ClientHandler.clients.get((idx + 1) % 
                                ClientHandler.clients.size()).getKey();
                        System.out.println("Hello   " + currUser);
                        System.out.println(" " + idx + " " + nextUser + " " + 
                                ((idx + 1) % ClientHandler.clients.size()));
                        ClientHandler.broadcast("CARD_" + msgs[1] + "_" + nextUser + "_" + currUser);
                        break;
                        
                    case "SURRENDER":
                        idx = getClientIdx();
                        String surrenderedUser = ClientHandler.clients.get(idx).getKey();
                        
                        if(surrenderedUser.equals(currUser)){
                            nextUser = ClientHandler.clients.get((idx + 1) % 
                                ClientHandler.clients.size()).getKey();
                            ClientHandler.broadcast("CARD_" + 0 + "_" + nextUser + "_" + currUser);
                        }
                        
                        ClientHandler.broadcast("NOTIFICATION_Surrender "+surrenderedUser +"\n");
                        
                        ClientHandler.clients.remove(idx);
                        
                        if(clients.size() <= 1){
                            cards = new int[] {4, 4, 4, 4, 4, 4};
                            
                            if(clients.size() == 1){
                                ClientHandler.clients.remove(1);
                                ClientHandler.broadcast("GAMEOVER_");
                            }
                        }
                        
                        return;
                        
                    case "CLOSEWINDOW":
                        
                        idx = getClientIdx();
                        String closedUser = ClientHandler.clients.get(idx).getKey();
                        
                        if(closedUser.equals(currUser)){
                            nextUser = ClientHandler.clients.get((idx + 1) % 
                                ClientHandler.clients.size()).getKey();
                            ClientHandler.broadcast("CARD_" + 0 + "_" + nextUser + "_" + currUser);
                        }
                        
                        idx = getClientIdx();
                        String username2 = ClientHandler.clients.get(idx).getKey();
                        ClientHandler.clients.remove(idx);
                        ClientHandler.broadcast("NOTIFICATION_Disconnected "+username2 +"\n");
                        
                        if(clients.size() <= 1){
                            cards = new int[] {4, 4, 4, 4, 4, 4};
                            if(clients.size() == 1){
                                ClientHandler.clients.remove(1);
                                ClientHandler.broadcast("GAMEOVER_");
                            }
                        }
                        return;
                    default:
                        System.out.println("!!!");
                        break;
                }

            }

        } catch (IOException e) {

        }
    }

}
