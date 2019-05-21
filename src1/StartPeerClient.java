import java.rmi.Naming;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.RMISecurityManager;
import java.util.*;
import java.rmi.NotBoundException;


public class StartPeerClient {
	private static final String Indsrv = "localhost";
	static String arg=null;
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

			//take 2 provided port as the arg for peerclientURL below		
			arg=args[1];
			String location = "rmi://"+Indsrv+":"+args[0]+"/peerserver";
			IndexServerInterface peerServer = (IndexServerInterface) Naming.lookup(location);
			PeerClient clientserver= new PeerClient(args[2],args[1],peerServer);

			//Better way to start Threads??
			new Thread(new peerfunction()).start();
			new Thread(clientserver).start();		
	}
	static class peerfunction implements Runnable
	{
		public void run()
		{			
			try {
				String clientloc = "rmi://"+Indsrv+":"+arg+"/clientserver";
				PeerFunctionInterface pcs = new PeerFunction();
				Naming.rebind(clientloc,pcs);
			} catch (RemoteException | MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}

//ARCHIVE

 /*
	public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
			
			this.directory = System.setProperty("java.rmi.server.hostname","localhost");
			File f = new File(directory);
			this.allFiles = f.list();
			
			
			//IndexServerInterface peerserver = (IndexServerInterface) Naming.lookup("rmi://localhost:" + args[0] +"/peerserver");
		


//------------------------------------------------------------------------------------------------------------------------
			//Needs to be broken out into it's on static class
			//static class ____ implements runnable

			
		}
		static class peerfunction implements Runnable{

			public void run(){
				try{
				//String clientURL = "rmi://" + Indsrv + ":5001/clientserver";
				String clientURL = "rmi://" + Indsrv + ":" + arg + "/clientserver";
			
				Naming.rebind(clientURL, fs);	
			// Below code prompts user at server end for file that client wants to request.
			//	Need to move function to client side
	
			//Not where this function needs to be
			
			Scanner scanner = new Scanner(System.in);
			System.out.print("File name: ");
			String filename = scanner.nextLine();

			fs.setFile(filename);
			
			//System.out.println("File Server is Ready");
			
			}	catch(Exception e){
				e.printStackTrace();
			}
		
			
		}
	}
}*/
