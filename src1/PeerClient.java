

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import java.util.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;


public class PeerClient  extends UnicastRemoteObject implements PeerClientInterface, Runnable {
	
        private static final long serialVersionUID = 1L;  

        public String peerPort = null;
        public String[] files;
        public String name = null;
        public String peername = null;
        public String filename = null;
        public String peerPath = null;

        //Bring in IndexServer
        private IndexServerInterface peerServer;

        protected PeerClient() throws RemoteException{
            super();
        }

//----------------------------------------------------------------------
//Constructor
        public PeerClient(String peername, String peerPort, IndexServerInterface peerServer) throws RemoteException{
            this.peername = peername;
            this.peerServer = peerServer;
            this.peerPort = peerPort;
            
            try{
            //peerPath is the directory for the peer    
            this.peerPath = System.getProperty("user.dir");
            File f = new File(peerPath);
            this.files = f.list();
            }
            catch(Exception e) {
                System.out.println("Peer path Exception caught =" +e.getMessage());
            }

            System.out.println(peerServer.addtoIndex(this));
		    new Thread(new IndexWatch(this)).start();
        }

        
        public String[] getFiles(){
            return files;
        }

        public String getName() throws RemoteException{
            return name;
        }

        public String getPeerPort(){
            return peerPort;
        }

        public IndexServerInterface peerServ(){
            return peerServer;
        }
        
        public String userDirec(){
            return peerPath;
        }
//------------------------------------------------------------------------

//Need to clean this up. Better UI cause running stragnely in Terminal
        public void run(){
            System.out.println("Successfully connected!!");
            String filename = null;
            Scanner scanner = new Scanner(System.in);

            //best way to keep scanner runnning until program closes
            while (true){
			    System.out.print("File name: ");
                filename = scanner.nextLine();
                PeerFunctionInterface peerfunctioninterface = null;
                
                    if (filename != null){
                        try{
                            PeerClientInterface[] match = fileLookup(filename);
                            //specify nextInt to look for user provided number for peer
                            int selectedPeer = scanner.nextInt();
                    
                            
                            String variableAddress = "rmi://localhost:" + match[selectedPeer - 1].getPeerPort() + "/clientserver";
                            peerfunctioninterface = (PeerFunctionInterface) Naming.lookup(variableAddress);
                        }
                        catch(RemoteException e) {
                            e.printStackTrace();
                        }
                        catch(NotBoundException e){
                            e.printStackTrace();  
                        }
                        catch(MalformedURLException e){
                            e.printStackTrace();  
                        }
                        initializeLogin(peerfunctioninterface, filename);
                    }
                else{
                    System.out.print("Error");
                }
        }
    }

        public void initializeLogin(PeerFunctionInterface sourcePeer, String filename){
                try{
                    //login is the download feature
                    if(sourcePeer.login(this, filename)){
                        System.out.println("File downloaded!!");
                    }
                    else{
                        System.out.println("Error");
                    }
                    }
                catch(RemoteException e){
                    e.printStackTrace();
                }

        }

        
        public synchronized PeerClientInterface[] fileLookup(String filename) {
            PeerClientInterface[] match = null;	
            try {
                match = peerServer.searchIndex(filename, name);
                if (match != null) {
                    System.out.println("Match/Matches found:");
                    for (int i=0; i<match.length; i++) {
                        if (match[i] != null)
                            System.out.println((i+1)+". "+match[i].getName());
                            
                    }
                    //prompt user to choose Peer to download from
                    System.out.println("Pick peer");
                    return match;
                } else {
                    System.out.println("Match not found");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;		
        }
      
        public synchronized void indexRegister() throws RemoteException{
            File f = new File(peerPath);
            this.files = f.list();
            if(peerServer.indexUpdate(this)){
                System.out.println("Updated Succesfully");
            }
            else{
                System.out.println("Error check.");
            }


        }

//From http://www.ejbtutorial.com/java-rmi/how-to-transfer-or-copy-a-file-between-computers-using-java-rmi
        public boolean sendData(String filename, byte[] data, int len) throws RemoteException{
            System.out.println("File Downloading");
            try{
                File f = new File(filename);
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(f,true);
                out.write(data,0,len);
                out.flush();
                out.close();
                System.out.println("Done writing data...");
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }

    
//Old RMI method want to keep for archive
  /*public static void main(String[] args) {

       
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);          
            IndexServerInterface stub = (IndexServerInterface) registry.lookup("Hello");

            //String response = stub.getHM();
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    */
    
}