package rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class RMIScoreServer {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry registery = LocateRegistry.createRegistry(123);
        ScoresImp score1 = new ScoresImp();
        registery.bind("getScore", score1);
        ScoresImp score2 = new ScoresImp();
        registery.bind("updateScore", score2);        
    }
    
}
