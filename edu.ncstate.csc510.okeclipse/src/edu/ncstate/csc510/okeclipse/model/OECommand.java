package edu.ncstate.csc510.okeclipse.model;

/**
 * 
 * @author Shrikanth N C
 * OE - Ok Eclipse command
 * A wrapper to eclipse command id and user spoken name
 *
 */
public final class OECommand {

	private String name;
	private String id;

	public OECommand(String command, String id) {
		super();
		this.name = command;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "OECommand [command=" + name + ", id=" + id + "]";
	}

}
