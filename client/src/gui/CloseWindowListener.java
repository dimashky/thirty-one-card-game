    package gui;

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
            // don't send if the client close the socket
            //if(!ServerMain.disconnect)
                Game.getInstance().send("CLOSEWINDOW_");
            // stop while(true) dis.read
            //ServerMain.disconnect = true;
        } catch (IOException ex) {
            Logger.getLogger(CloseWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.getWindow().dispose();
    } 
}
