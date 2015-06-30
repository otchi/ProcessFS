package com.edifixio.ProcessFS;

public class FSMove {
	private String from;
	private String to;
	private FSMove(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}

}
