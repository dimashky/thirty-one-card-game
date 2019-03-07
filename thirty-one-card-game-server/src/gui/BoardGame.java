package gui;

import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;

public class BoardGame {

    private static BoardGame instance;
    private JFrame mainFrame;
    private JButton[] cardButtons;
    private JLabel[] cardRemainLabes;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private int[] remainCards;
    private int sum = 0;
    private Timer time = new Timer();
    private boolean myTurn = true;

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
        turnLabel.setForeground(SystemColor.green);
        scoreLabel = new JLabel("Score = 0");
        timerLabel = new JLabel("");
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

        updateTimer(10);

        turnLabel.setBounds(10, 220, 100, 15);
        mainFrame.getContentPane().add(turnLabel);

        scoreLabel.setBounds(10 + 110 * 2, 220, 100, 15);
        mainFrame.getContentPane().add(scoreLabel);

        timerLabel.setBounds(10 + 110 * 4, 220, 100, 15);
        mainFrame.getContentPane().add(timerLabel);
    }

    public void setVisible(Boolean arg0) {
        this.mainFrame.setVisible(arg0);
    }

    public boolean update(int cardNumber, Boolean myTurn) {

        int idx = cardNumber - 1;

        if (remainCards[idx] == 0) {
            System.out.println("Cards cannot be less than zero");
            return false;
        }
        updateTimer(10);

        sum += cardNumber;

        scoreLabel.setText("Score = " + sum);

        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));

        setTurn(myTurn);

        if (this.isGameOver(sum, myTurn)) {
            System.out.print("Game Over");
            updateTimer(0);
            JOptionPane.showMessageDialog(null, "Game Over");
            for (int i = 0; i < 6; i++) {
                cardButtons[i].setEnabled(false);
            }
            return true;
        }

        return false;
    }

    public void setTurn(boolean myTurn) {

        if (myTurn) {
            turnLabel.setText("Rival's Turn");
            turnLabel.setForeground(SystemColor.red);
        } else {
            turnLabel.setText("Your Turn");
            turnLabel.setForeground(SystemColor.green);
        }

        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(!myTurn);
        }

        this.myTurn = !myTurn;
    }

    private void updateTimer(int interval_arg){

        int delay = 1000;
        int period = 1000;
        time.cancel();
        time.purge();
        time = new Timer();

        timerLabel.setText(String.valueOf(interval_arg));

        BoardGame board = this;

        time.scheduleAtFixedRate(new TimerTask() {

            int interval = interval_arg;

            public void run() {
                if (interval == 0) {
                    timerLabel.setText("Time Finished");
                    time.cancel();
                    time.purge();

                    if(!myTurn){
                        return;
                    }

                    for(int i = 0; i < 6; ++i) {
                        if(board.remainCards[i] > 0) {
                            board.update(i+1, true);
                            try {
                                Game.getInstance().send("CARD_" + (i+1));
                            } catch (Exception ex) {

                            }

                            return;
                        }
                    }

                } else {
                    interval--;
                    timerLabel.setText("" + interval);
                }
            }
        }, delay, period);
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
