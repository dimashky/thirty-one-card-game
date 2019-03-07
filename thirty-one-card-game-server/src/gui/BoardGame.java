package gui;

import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import listeners.CardButtonListener;

public class BoardGame {

    private static BoardGame instance;
    private JFrame mainFrame;
    private JButton[] cardButtons;
    private JLabel[] cardRemainLabes;
    private JLabel turnLabel;
    private int[] remainCards;
    private boolean myTurn;

    // singleton class
    static public BoardGame getInstance() {
        if (instance == null) {
            instance = new BoardGame();
        }
        return instance;
    }

    private BoardGame() {
        mainFrame = new JFrame();
        cardButtons = new JButton[6];
        cardRemainLabes = new JLabel[6];
        turnLabel = new JLabel("Your Turn");
        remainCards = new int[6];

        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Server");
        mainFrame.setBounds(100, 100, 700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);

        cardButtons = new JButton[6];
        cardRemainLabes = new JLabel[6];

        for (int i = 0; i < 6; ++i) {
            cardButtons[i] = new JButton(new Integer(i + 1).toString());
            cardButtons[i].setBackground(SystemColor.control);
            cardButtons[i].setBounds(10 + 110 * i, 10, 100, 160);
            mainFrame.getContentPane().add(cardButtons[i]);

            cardButtons[i].addActionListener(new CardButtonListener(i + 1));

            cardRemainLabes[i] = new JLabel("4", JLabel.CENTER);
            cardRemainLabes[i].setBounds(10 + 110 * i, 180, 100, 15);
            mainFrame.getContentPane().add(cardRemainLabes[i]);

            remainCards[i] = 4;
        }

        turnLabel.setBounds(10, 220, 100, 15);
        turnLabel.setForeground(SystemColor.green);

        mainFrame.getContentPane().add(turnLabel);
    }

    public void setVisible(Boolean arg0) {
        this.mainFrame.setVisible(arg0);
    }

    public boolean update(int cardNumber, Boolean myTurn) {

        int idx = cardNumber - 1;
        int sum = 0;

        if (remainCards[idx] == 0) {
            System.out.println("Cards cannot be less than zero");
            return false;
        }

        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));

        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(!myTurn);
            sum += (4 - remainCards[i]) * (i + 1);
        }

        setTurn(myTurn);

        if (this.isGameOver(sum, myTurn)) {
            System.out.print("Game Over");
            return true;
        }

        return false;
    }

    public void setTurn(boolean myTurn) {
        this.myTurn = myTurn;

        if (myTurn) {
            turnLabel.setText("Rival's Turn");
            turnLabel.setForeground(SystemColor.red);
        } else {
            turnLabel.setText("Turn Turn");
            turnLabel.setForeground(SystemColor.green);
        }

        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(!myTurn);
        }
    }

    private Boolean isGameOver(int sum, boolean turn) {
        if (sum < 31) {
            return false;
        }

        if (turn) {
            turnLabel.setText("You loose");
            turnLabel.setForeground(SystemColor.red);
        } else {
            turnLabel.setText("You Win");
            turnLabel.setForeground(SystemColor.green);
        }
        return true;
    }

}
