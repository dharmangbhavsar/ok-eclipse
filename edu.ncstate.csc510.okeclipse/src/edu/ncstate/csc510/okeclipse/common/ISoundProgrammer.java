package edu.ncstate.csc510.okeclipse.common;

import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;

/**
 * 
 * @author Shrikanth N C
 * Abstract Sound programmer functionalities ( refer doc
 *         for more details )
 */
public interface ISoundProgrammer {

	/**
	 * 
	 * @param javaSourceCode
	 * @throws BadLocationException
	 * @throws IOException
	 */
	public void injectCode(String javaSourceCode) throws BadLocationException, IOException;

	/**
	 * 
	 * @return
	 */
	public String generateMainMethod();

	/**
	 * 
	 * @param javaSourceCode
	 * @return
	 */
	public String generateGetterSetter(String javaSourceCode);

	/**
	 * 
	 * @param javaSourceCode
	 * @param variableName
	 * @return
	 */
	public String implementInterface(String javaSourceCode, String variableName);

	/**
	 * 
	 * @param variable
	 * @return
	 */
	public String generateSort(String variable);

	/**
	 * 
	 * @param content
	 *            - string to insert
	 * @param position
	 *            - index in the file to insert
	 * @throws BadLocationException
	 */
	public void insertContent(String content, int position) throws BadLocationException;

}
