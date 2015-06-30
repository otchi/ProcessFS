package com.edifixio.ProcessFS;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

public class FSEvent {

	private Kind<Path>  eventKind;
	private String eventPath;
		
	public FSEvent(Kind<Path> entryDelete, String eventPath) {
		super();
		this.eventKind = entryDelete;
		this.eventPath = eventPath;
	}

	public Kind<Path>  getEventKind() {
		return eventKind;
	}
	
	public String getEventPath() {
		return eventPath;
	}
		
}
