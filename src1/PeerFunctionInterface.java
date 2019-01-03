
import java.rmi.Remote;
import java.rmi.RemoteException;




public interface PeerFunctionInterface extends Remote {
    public String getName() throws RemoteException;
    public boolean login(PeerClientInterface c, String filename) throws RemoteException;
    //String sayHello() throws RemoteException;
    //String getHM() throws RemoteException;
}