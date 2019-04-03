package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScoresInterface extends Remote {

    public int getScore(String user) throws RemoteException;
    
    public boolean updateScore(String user) throws RemoteException;
    
}
