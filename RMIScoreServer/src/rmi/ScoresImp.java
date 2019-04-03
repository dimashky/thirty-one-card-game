package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoresImp extends UnicastRemoteObject implements ScoresInterface {

    Database db =new Database();
    Connection con = db.getConnection();
    PreparedStatement pst;
    
    public ScoresImp() throws RemoteException{  }
    
    private int newUser(String user){
        try {
            String sql = "INSERT INTO userscore (username, score) VALUES( ?, ? );";
            pst = db.getConnection().prepareStatement(sql);    
            pst.setString(1, user);
            pst.setInt(2, 0);
            pst.execute();           
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
    @Override
    public int getScore(String user) throws RemoteException {    
        try {
            String sql = "SELECT * FROM userscore WHERE username = ?";
            pst = db.getConnection().prepareStatement(sql);    
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("score");
            } else {
                return newUser(user);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean updateScore(String user) throws RemoteException {
        int x = getScore(user);
        try {
            String sql = "Update userscore set score = ? where username = ? ";        
            pst = db.getConnection().prepareStatement(sql);
            pst.setString(2, user);
            pst.setInt(1, ++x);
            pst.executeUpdate();      
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
}
