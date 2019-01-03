

import java.rmi.Remote;
import java.rmi.RemoteException;

 


public interface PeerClientInterface extends Remote{
    IndexServerInterface peerServ() throws RemoteException;
    public void indexRegister() throws RemoteException;
    public String userDirec() throws RemoteException;
    public String getPeerPort() throws RemoteException;
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException;
    public String getName() throws RemoteException;
    public String[] getFiles() throws RemoteException;
    
}