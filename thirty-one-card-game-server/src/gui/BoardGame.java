package gui;

import java.awt.Image;
import java.awt.SystemColor;
import java.io.FileInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
    private JButton surrenderButton;
    private JButton rematchButton;

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
        surrenderButton = new JButton("Surrender");
        rematchButton = new JButton("Rematch");
        
        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Server");
        mainFrame.setResizable(false);
        mainFrame.setBounds(100, 100, 700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);

        mainFrame.addWindowListener(new CloseWindowListener());

        for (int i = 0; i < 6; ++i) {
            cardButtons[i] = new JButton(Integer.toString(i + 1));
            cardButtons[i].setBackground(SystemColor.control);
            cardButtons[i].setBounds(10 + 110 * i, 10, 100, 160);
            mainFrame.getContentPane().add(cardButtons[i]);

            cardButtons[i].addActionListener(new CardButtonListener(i + 1));

            cardRemainLabes[i] = new JLabel("4", JLabel.CENTER);
            cardRemainLabes[i].setBounds(10 + 110 * i, 180, 100, 15);
            mainFrame.getContentPane().add(cardRemainLabes[i]);

            remainCards[i] = 4;
        }

        updateTimer(30);

        turnLabel.setBounds(10, 220, 100, 15);
        mainFrame.getContentPane().add(turnLabel);

        scoreLabel.setBounds(10 + 110 * 2, 220, 100, 15);
        mainFrame.getContentPane().add(scoreLabel);

        timerLabel.setBounds(10 + 110 * 4, 220, 100, 15);
        mainFrame.getContentPane().add(timerLabel);
        
        surrenderButton.setBounds(10 + 110 * 5, 220, 100, 15);
        mainFrame.getContentPane().add(surrenderButton);
        surrenderButton.addActionListener(new SurrenderButtonListener());
        
        rematchButton.setBounds(10 + 110 * 5, 240, 100, 15);
        mainFrame.getContentPane().add(rematchButton);
        rematchButton.addActionListener(new RematchButtonListener());
        rematchButton.setEnabled(false);
        
    }

    public void setVisible(Boolean arg0) {
        this.mainFrame.setVisible(arg0);
    }

    public void rematch(Boolean turn){
        for(int i =0; i < 6; i++){
            remainCards[i] = 4;
            cardRemainLabes[i].setText(String.valueOf(remainCards[i]));
        }
        updateTimer(30);
        setTurn(turn);
        rematchButton.setEnabled(false);
        sum = 0;
        scoreLabel.setText("Score = " + 0);
    }
    
    public boolean update(int cardNumber, Boolean myTurn) {

        int idx = cardNumber - 1;

        if (remainCards[idx] == 0) {
            System.out.println("Cards cannot be less than zero");
            return false;
        }
        updateTimer(30);

        sum += cardNumber;

        scoreLabel.setText("Score = " + sum);

        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));

        setTurn(myTurn);

        if (this.isGameOver(sum, myTurn)) {
            System.out.print("Game Over");
            updateTimer(0);

            for (int i = 0; i < 6; i++) {
                cardButtons[i].setEnabled(false);
            }
            rematchButton.setEnabled(true);
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

        setEnableAllButtons(!myTurn);
        
        this.myTurn = !myTurn;
    }

    private void setEnableAllButtons(boolean enable){
        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(enable);
        }
        surrenderButton.setEnabled(enable);
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

                    if(!myTurn || board.sum >= 31){
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

    public void finishTheGame(int cardNumber){
        time.cancel();
        int idx = cardNumber - 1;
        sum += cardNumber;
        scoreLabel.setText("Score = " + sum);

        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));
        
        setTurn(false);
        surrenderButton.setEnabled(false);
         rematchButton.setEnabled(true);
        turnLabel.setText("You Win");
        JOptionPane.showMessageDialog(null, "Game Over");

    }

    public void disconnected(){
        time.cancel();
        setEnableAllButtons(false);
        rematchButton.setEnabled(false);
        turnLabel.setText("Rival Disconnected");
        turnLabel.setForeground(SystemColor.PINK);
    }
    
    public void surrender(Boolean turn){
        time.cancel();
        setEnableAllButtons(false);
        rematchButton.setEnabled(true);
        if(turn){
            turnLabel.setText("You Surrendered");
            turnLabel.setForeground(SystemColor.MAGENTA);
        }else{
            turnLabel.setText("You Win, Rival Surrender");
            turnLabel.setForeground(SystemColor.CYAN);
        }
    }
    
    
}
