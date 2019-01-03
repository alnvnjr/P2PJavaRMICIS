
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public interface IndexServerInterface extends Remote {
    public boolean indexUpdate(PeerClientInterface newPeer) throws RemoteException;
	public String addtoIndex(PeerClientInterface newPeer) throws RemoteException;
	PeerClientInterface[] searchIndex(String file, String peerInitial) throws RemoteException;
    //String[] getFiles() throws RemoteException;
    //String peerArray() throws RemoteException;
    
}