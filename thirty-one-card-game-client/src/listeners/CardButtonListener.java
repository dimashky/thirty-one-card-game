package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.BoardGame;
import gui.Game;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardButtonListener implements ActionListener {

    private int cardNumber;

    public CardButtonListener(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        BoardGame.getInstance().update(cardNumber, true);
        try {
            Game.getInstance().send(cardNumber);
        } catch (IOException ex) {
            Logger.getLogger(CardButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
