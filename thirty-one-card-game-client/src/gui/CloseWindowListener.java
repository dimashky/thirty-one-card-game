package gui;

import client.ClientMain;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseWindowListener extends WindowAdapter{
    
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        System.out.println("Closed");
        try {
            if(!ClientMain.disconnect)
                Game.getInstance().send("CLOSEWINDOW_");
            ClientMain.disconnect = true;
            //Game.getInstance().closeConnection();
        } catch (IOException ex) {
            Logger.getLogger(CloseWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.getWindow().dispose();
    } 
}
