
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//import javafx.beans.binding.StringBinding;

import java.util.*;


public class IndexServer extends UnicastRemoteObject implements IndexServerInterface{

    private static final long serialVersionUID = 1L;
    //Create the Array
    private ArrayList<PeerClientInterface> peers = new ArrayList<PeerClientInterface>();
    public IndexServer() throws RemoteException{
        super();
    }
	//private HashMap<String, PeerClientInterface> peers = new HashMap<String, PeerClientInterface>();
    //for HashMap
    /*
    public String peerArray() throws RemoteException{
        System.out.println("Peers Below");
        if (peers.keySet() != null){
            System.out.println("Peer Hashmap not empty");
            for (String item:peers.keySet()) {
                System.out.println("Entered for loop.");
                String key = item.toString();
                String value = peers.get(item).toString();
                return "PeerID: " + key + "----" + "PeerName: " + value;
                //System.out.println("PeerID: "+peers.get(count));
            }
            return "------------";
        }
        else{
            
            return "No registered peers";
        }     
    }
	*/

	//build array first
    private void peerArray() throws RemoteException {
		for (int a=0; a<peers.size(); a++ ) {
			System.out.println("Name: "+ peers.get(a).getName());
			System.out.println("Files: ");
            String[] peerfiles = peers.get(a).getFiles();
            //within the peer, what files
			for (int i=0; i<peerfiles.length; i++) {
				System.out.println("\n"+peerfiles[i]);
			}
		}
	}


    //for HashMap
    /*
    public boolean updatePeerClient(PeerClientInterface peerClient) throws RemoteException {
        //Update the server with changes has been made 
        
		for (String item:peers.keySet()) {
			if(peerClient.getName().equals(peers.get(item).getName())){
                peers.remove(item);
                String key = item.toString();
                peers.put(key, peerClient);
            }
		}
		System.out.println("\n\nServer has been updated. See list of registered Peers Below:");
		peerArray();
		return true;
	}
	*/
  

    //search for file
    public synchronized PeerClientInterface[] searchIndex(String file, String peerInitial) throws RemoteException {
		Boolean find = false;
		PeerClientInterface[] peer = new PeerClientInterface[peers.size()];
		int count = 0;
		for (int l=0; l<peers.size(); l++ ) {
			String[] peersString = peers.get(l).getFiles();
			for (int a=0; a<peersString.length; a++) {
				if (file.equals(peersString[a])) {
					find = true;
					peer[count] = (PeerClientInterface) peers.get(l);
					count++;
				}
			}
		}
		if(find) {
			System.out.println("FileFound in" + peerInitial + "system.");
			return peer;
		} else
			System.out.println("File not found");
			return peer;
	}
	
	//Need to have synchronzed for multiple uploads
    public synchronized String addtoIndex(PeerClientInterface newPeer) throws RemoteException {
		
		//add new peer to peers Array
		peers.add(newPeer);	  	
		
		String addedFiles = "";
		String[] files = newPeer.getFiles();
		//Loop through and add all files, display	
		for (int i=0; i<files.length; i++){
			addedFiles += "\n"+files[i];
		}
		System.out.println("Peer Registered with Index.");
		peerArray();
		return newPeer.getName() + ":" + addedFiles +" = added";
	}

		//Add changes to the index. 
		public boolean indexUpdate(PeerClientInterface newPeer) throws RemoteException {
		
			for (int a=0; a<peers.size(); a++ ) {
				//if(newPeer.getName() == peers.get(a).getName())
				//seems like error returned

				if(newPeer.getName().equals(peers.get(a).getName())){
					//have to remove first. Error returned otherwise.
					peers.remove(a);
					peers.add(newPeer);
				}
			}
			System.out.println("Server updated");
			peerArray(); 	
			return true;
		}
	

}