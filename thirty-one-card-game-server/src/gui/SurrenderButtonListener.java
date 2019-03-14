package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SurrenderButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            Game.getInstance().send("SURRENDER_");
            BoardGame.getInstance().surrender(true);
        } catch (IOException ex) {
            Logger.getLogger(SurrenderButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}