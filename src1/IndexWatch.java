

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class IndexWatch implements Runnable {
	private PeerClientInterface peerClient;
	public IndexWatch(PeerClient peerClient) {
		
		this.peerClient = peerClient;
	}

	@Override
	public void run() {
		try {
			
			
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(peerClient.userDirec());
		    WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
		    			StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            
//Source: https://docs.oracle.com/javase/tutorial/essential/io/notification.html
   
		    for (;;) {
		    	
		        try {
		        	key = watcher.take();
		        } catch (InterruptedException x) {
		            return;
		        }
		        boolean newFile = false;
		        for (WatchEvent<?> event: key.pollEvents()) {
		            WatchEvent.Kind<?> kind = event.kind();				            
		         
		            if (kind == StandardWatchEventKinds.OVERFLOW) {
		                 continue;
		                }
		            if (kind==StandardWatchEventKinds.ENTRY_DELETE || kind==StandardWatchEventKinds.ENTRY_MODIFY){
		            	peerClient.indexRegister();
		                }
		            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
		            	if(newFile)
		            		peerClient.indexRegister();
		            	else
		            		newFile = true;
		            }
		        }

		        boolean valid = key.reset();
		        if (!valid) {
		            break;
		        }
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
	}	
}