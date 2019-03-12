package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RematchButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            //TODO add action to game board
            Game.getInstance().send("REMATCH");
        } catch (IOException ex) {
            Logger.getLogger(RematchButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
