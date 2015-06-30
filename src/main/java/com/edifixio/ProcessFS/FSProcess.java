package com.edifixio.ProcessFS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class FSProcess extends Thread {
	
	
	private File directory;
	private HashMap<String, FSProcess> childDirectoryProcesses=new HashMap<String, FSProcess>();
	private static Hashtable<String, FSProcess> allProcess=new Hashtable<String, FSProcess>();
	private static Hashtable<String, FSMove> globalEvent=new Hashtable<String, FSMove>();
	private ArrayList<FSEvent> localEvent=new ArrayList<FSEvent>();
	private static Collection<ListenNotifyProcess> toNotify=Collections.synchronizedCollection(new ArrayList<FSProcess.ListenNotifyProcess>());


	

	
	
	 private FSProcess(File directory) {
		super();
		this.directory = directory;
		System.out.println(this.directory.getPath());
	}
	 
	 public static FSProcess  _constructWatchFS(String directoryPath){
		File f=new File(directoryPath);
		FSProcess processFS=f.isDirectory()?  new FSProcess(f): null;
		if(processFS!=null)recursiveProcess(processFS);
		return processFS;
	 }
	 
	 
	 private static void recursiveProcess(FSProcess processFS){
		
		processFS.start();
		
		
		 File[] files=processFS.directory.listFiles();
		 for(File file:files){
			 if(file.isDirectory()){
				 FSProcess process_FS =new FSProcess(file);
				 processFS.childDirectoryProcesses
				 	.put(file.getPath(), process_FS);
				 allProcess
				 	.put(file.getAbsolutePath(), process_FS);
				recursiveProcess(process_FS);}
			 
		 } 
	 }
	 
	@Override 
	public void run() {	 
		Path myDir = Paths.get(this.directory.getAbsolutePath()); 
		
		    while(true){	
		    	try {
		    		WatchService watcher = myDir.getFileSystem().newWatchService();
					myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_DELETE,
							StandardWatchEventKinds.ENTRY_MODIFY);
		
					WatchKey watckKey = watcher.take();
		 
					List<WatchEvent<?>> events = watckKey.pollEvents();
		    		
					for ( WatchEvent<?> event : events) {
		    			
		    			if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
		    			
		    			} else
		    				
		    			if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
		    					System.out.println("Delete: " + event.context().toString());
		    
		    			} else
		    
		    			if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
		    				localEvent.add(new FSEvent(StandardWatchEventKinds.ENTRY_MODIFY,
		    							event.context().toString()));
		    			}
		    			
		    			
		    		}
		    		
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
	
		
	 }

/*******************************************************************************************/
	
	private class ListenNotifyProcess extends Thread {
		private FSProcess parentProcess;
		private String localPath;
		List<Notify> listNotification = Collections.synchronizedList(new ArrayList<Notify>());
		
		private ListenNotifyProcess( FSProcess parentProcess){
			this.parentProcess=parentProcess;
		}
		@Override
		public void run() {		
			try {
				Thread.sleep(10);
				
				parentProcess.localEvent.add(new FSEvent(StandardWatchEventKinds.ENTRY_DELETE,
												localPath) );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
		
		private synchronized void notifyProcess(Notify notify){
			listNotification.add(notify);
		}

	}
/*******************************************************************************************/
	private class Notify extends Thread{
		
		private FSProcess parentProcess;
		private String localPath;
		private Boolean isMove=false;
		
		private Notify(String path,FSProcess parentProcess) {
			super();
			this.localPath = path;
			this.parentProcess=parentProcess;
			Iterator<ListenNotifyProcess> iterNotif=toNotify.iterator();
			while(iterNotif.hasNext()){
					iterNotif.next().notifyProcess(this);
			}
		}
		
		private synchronized void moved(){
			isMove=true;
		}
		
		@Override
		public void run(){
			try {
				Thread.sleep(50);
				if(!this.isMove);
					this.parentProcess.localEvent
							.add(new FSEvent(StandardWatchEventKinds.ENTRY_CREATE,localPath));
					
				this.interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}	
/********************************************************************************************/
	
	public static void main (String args[]){
		
	//	FSProcess._constructWatchFS("C:\\Users\\aoa\\Desktop\\test");
		//System.out.println(Thread.MIN_PRIORITY);
	}
	 
	 


}
