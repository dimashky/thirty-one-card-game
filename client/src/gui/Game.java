package gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Game {

	private static Game instance = null;
	public BoardGame board;
	private static Socket client;
	DataOutputStream dos;

	static public Game getInstance() throws IOException {
		if (instance == null) {
			instance = new Game(client, "user");
		}
		return instance;
	}

	public Game(Socket client, String username) throws IOException {
		instance = this;
		Game.client = client;
		initGame(false, username);
		dos = new DataOutputStream(client.getOutputStream());
	}

	public void send(String msg) throws IOException {
		dos.writeUTF(msg);
	}

	public boolean update(int cardNumber, boolean turn, String s, Boolean b) {
		return BoardGame.getInstance().update(cardNumber, turn, s, b);
	}

	public void finishTheGame(int cardNumber) {
		BoardGame.getInstance().finishTheGame(cardNumber);
	}

	public void rivalSurrender() {
		BoardGame.getInstance().rivalSurrender();
	}

	public void initGame(boolean turn, String username) {
		board = BoardGame.getInstance();
		board.mainFrame.setTitle(username);
		board.setVisible(true);
	}

}
