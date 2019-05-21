
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;




public class PeerFunction extends UnicastRemoteObject implements PeerFunctionInterface {

    
    private static final long serialVersionUID = 1L;
    //private String file = "";

    protected PeerFunction() throws RemoteException{
        super();
    }
    @Override
    public String getName() throws RemoteException{
        return "allen";
    }
    /* This part might be unnecessary. 
    public void setFile(String f){
        file = f;
    }
    */


//Source: http://www.ejbtutorial.com/java-rmi/how-to-transfer-or-copy-a-file-between-computers-using-java-rmi
    public synchronized boolean login(PeerClientInterface c, String file) throws RemoteException{
        //Sending File...

        try{
            File f1 = new File(file);			 
            FileInputStream in=new FileInputStream(f1);			 				 
            byte [] mydata=new byte[1024*1024];						
            int mylen=in.read(mydata);
            while(mylen>0){  
                if(c.sendData(f1.getName(), mydata, mylen)){
                    System.out.println("File Sent!");	 
                    mylen=in.read(mydata);	
                }
                else{
                    System.out.println("File not sent?");
                }			 
            }
        }catch(Exception e){
            e.printStackTrace();   
        }
       return true;
    }

    //ARCHIVE

/*
   public static void main(String args[]) {

        try {

            PeerFunction obj = new PeerFunction();
            IndexServerInterface stub = (IndexServerInterface) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
       
            //registry.bind("HashMap", stub);
            registry.bind("Hello", stub);

            System.err.println("Server ready");
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    */
}