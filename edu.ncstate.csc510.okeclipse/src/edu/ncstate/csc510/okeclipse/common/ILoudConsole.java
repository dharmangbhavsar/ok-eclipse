package edu.ncstate.csc510.okeclipse.common;

import java.util.List;

/**
 * 
 * @author Shrikanth N C 
 * Abstract loud console functionalities ( refer doc
 *         for more details )
 */
public interface ILoudConsole {

	public List<String> extract(String consoleLog);

	public String getConsoleContent();

}
