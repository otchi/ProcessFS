package com.edifixio.ProcessFS;

import java.io.File;
import java.nio.file.StandardWatchEventKinds;
import java.util.HashMap;

public class ProcessFS extends Thread {
	
	private File directory;
	private HashMap<String, ProcessFS> childDirectoryProcesses=new HashMap<String, ProcessFS>();
	private static HashMap<String, StandardWatchEventKinds> events=new HashMap<String, StandardWatchEventKinds>();
	

	
	
	 private ProcessFS(File directory) {
		super();
		this.directory = directory;
		System.out.println(this.directory.getPath());
	}
	 
	 public static ProcessFS  _constructWatchFS(String directoryPath){
		File f=new File(directoryPath);
		ProcessFS processFS=f.isDirectory()?  new ProcessFS(f): null;
		if(processFS!=null)contructRecursiveProcess(processFS);
		return processFS;
		
		 
	 }
	 
	 
	private static void contructRecursiveProcess(ProcessFS processFS){
		
		processFS.start();
		 File[] files=processFS.directory.listFiles();
		 for(File file:files){
			 if(file.isDirectory()){
				 ProcessFS process_FS =new ProcessFS(file);
				 processFS.childDirectoryProcesses
				 	.put(file.getPath(), process_FS);
				 contructRecursiveProcess(process_FS);}
			 
		 } 
	 }
	 
	 
	public void run() {
		    // faire quelque chose	 	
		    while( true) {
		    	System.out.println(this.getName());
		    	try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		
	 }
	
	
	public static void main (String args[]){
		
		ProcessFS._constructWatchFS("C:\\Users\\aoa\\Desktop\\test");
		
	}
	 
	 


}
