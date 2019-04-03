package gui;

import java.awt.Image;
import java.awt.SystemColor;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class BoardGame {

    private static BoardGame instance = null;
    public JFrame mainFrame;
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
    private JTextArea textArea;
    
    public void setTextArea(String s){
        String old = textArea.getText();
        String newS = s + old;
        textArea.setText(newS);
    }
    
    // singleton class
    static public BoardGame getInstance() {
        if (instance == null) {
        	System.out.println("CREATE INSTANCE!");
            instance = new BoardGame();
        }
        return instance;
    }

    private BoardGame() {
        mainFrame = new JFrame();
        cardButtons = new JButton[6];
        cardRemainLabes = new JLabel[6];
        turnLabel = new JLabel("WAIT SERVER");
        turnLabel.setForeground(SystemColor.red);
        scoreLabel = new JLabel("Score = 0");
        timerLabel = new JLabel("");
        remainCards = new int[6];
        surrenderButton = new JButton("Surrender");
        rematchButton = new JButton("Rematch");
        textArea = new JTextArea( );
        
        initialize();
    }
    
    public void initCards(int[] cards){
        remainCards = cards;
        for(int i = 0; i < 6; i++){
            cardRemainLabes[i].setText(String.valueOf(cards[i]));
            sum += ( 4* (i+1) - cards[i]*(i+1));
        }
        scoreLabel.setText("Score = "+sum);
    }

    private void initialize() {
        System.out.println("INIT");
        mainFrame = new JFrame();
        mainFrame.setBounds(100, 100, 700, 360);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(null);

        
        mainFrame.addWindowListener(new CloseWindowListener());
        
        try {
            mainFrame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new FileInputStream("resources/board.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 6; ++i) {
            cardButtons[i] = new JButton(Integer.toString(i + 1));
            cardButtons[i].setBackground(SystemColor.control);
            cardButtons[i].setBounds(10 + 110 * i, 50, 100, 160);
            cardButtons[i].setEnabled(false);
            cardButtons[i].setOpaque(false);
            cardButtons[i].setContentAreaFilled(false);
            cardButtons[i].setBorderPainted(false);
            
            try {
                Image img = ImageIO.read(new FileInputStream("resources/c"+(i+1)+".bmp"));
                cardButtons[i].setIcon(new ImageIcon(img));
              } catch (Exception ex) {
                System.out.println("EXCEPTION " + ex.toString());
              }
            
            mainFrame.getContentPane().add(cardButtons[i]);

            cardButtons[i].addActionListener(new CardButtonListener(i + 1));

            cardRemainLabes[i] = new JLabel("4", JLabel.CENTER);
            cardRemainLabes[i].setBounds(10 + 110 * i, 220, 100, 15);
            cardRemainLabes[i].setForeground(SystemColor.white);
            
            mainFrame.getContentPane().add(cardRemainLabes[i]);

            remainCards[i] = 4;
        }

        //updateTimer(30);

        turnLabel.setBounds(10, 270, 100, 15);
        mainFrame.getContentPane().add(turnLabel);

        scoreLabel.setBounds(10 + 110 / 2, 15, 100, 15);
        scoreLabel.setForeground(SystemColor.white);
        mainFrame.getContentPane().add(scoreLabel);

        timerLabel.setBounds(10 + 110 * 4, 15, 100, 15);
        timerLabel.setForeground(SystemColor.white);
        mainFrame.getContentPane().add(timerLabel);
        
        surrenderButton.setBounds(10 + 110 * 5, 270, 100, 15);
        mainFrame.getContentPane().add(surrenderButton);
        surrenderButton.addActionListener(new SurrenderButtonListener());
        surrenderButton.setEnabled(false);
    
        textArea.setBounds(10 , 290, 400,200);
        
        mainFrame.getContentPane().add(textArea);
        
    }

    public void setVisible(Boolean arg0) {
        this.mainFrame.setVisible(arg0);
    }

    public boolean update(int cardNumber, Boolean myTurn, String currentUser, Boolean prevUser) {

        int idx = cardNumber - 1;

        if (idx >= 0 && remainCards[idx] == 0) {
            System.out.println("Cards cannot be less than zero");
            return false;
        }
        updateTimer(30);

        sum += cardNumber;

        scoreLabel.setText("Score = " + sum);

        setTurn(myTurn, currentUser);
        
        if(idx < 0){
            return false;
        }
        
        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));

        if(remainCards[idx] == 0) {
            try {
                Image img = ImageIO.read(new FileInputStream("resources/empty.bmp"));
                cardButtons[idx].setIcon(new ImageIcon(img));
              } catch (Exception ex) {
                System.out.println("EXCEPTION " + ex.toString());
              }
        }
        
        

        if (this.isGameOver(sum, prevUser)) {
            System.out.print("Game Over");
            updateTimer(0);
            //TODO here i move the dialog
//            JOptionPane.showMessageDialog(null, "Game Over");
            for (int i = 0; i < 6; i++) {
                cardButtons[i].setEnabled(false);
            }
            return true;
        }

        return false;
    }

    public void setTurn(boolean myTurn, String playerName) {

        if (myTurn) {
            turnLabel.setText(playerName);
            turnLabel.setForeground(SystemColor.red);
        } else {
            turnLabel.setText("Your Turn");
            turnLabel.setForeground(SystemColor.green);
        }

        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(!myTurn);
        }
        
        surrenderButton.setEnabled(!myTurn);
        
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

                    if(!myTurn || board.sum >= 31){
                        return;
                    }

                    for(int i = 0; i < 6; ++i) {
                        if(board.remainCards[i] > 0) {
                         //   board.update(i+1, true);
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
        rematchButton.setEnabled(true);
        return true;
    }

    public void finishTheGame(int cardNumber){
        time.cancel();
        int idx = cardNumber - 1;
        sum += cardNumber;
        scoreLabel.setText("Score = " + sum);

        remainCards[idx]--;
        cardRemainLabes[idx].setText(String.valueOf(remainCards[idx]));

        setTurn(false,"");
        turnLabel.setText("You Win");
        JOptionPane.showMessageDialog(null, "Game Over");

    }
    
    public void rivalSurrender() {
        time.cancel();
        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(false);
        }
        surrenderButton.setEnabled(false);
        turnLabel.setText("You Win, Rival Surrender");
        turnLabel.setForeground(SystemColor.CYAN);
    }
    
    public void stopTimer(){
        time.cancel();
        for (int i = 0; i < 6; i++) {
            cardButtons[i].setEnabled(false);
        }
        surrenderButton.setEnabled(false);
        turnLabel.setText("You Surrendered");
        turnLabel.setForeground(SystemColor.MAGENTA);
    }


}
