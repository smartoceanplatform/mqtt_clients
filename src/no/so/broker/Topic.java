package no.so.broker;

public class Topic{

	String name;
	String dimension;
	boolean isroot;
	Topic parent;

	public Topic(Topic parent, String name, String dimension) {
		this.name = name;
		this.dimension = dimension;
		this.parent = parent;
	}

	public boolean isroot() {
		return this.isroot;
	}
	
	public Topic getParent() {
		return this.parent;
	}
}
