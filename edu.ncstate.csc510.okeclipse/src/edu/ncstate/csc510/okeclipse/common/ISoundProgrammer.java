package edu.ncstate.csc510.okeclipse.common;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jgit.api.errors.GitAPIException;

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
	
	public void testCode(String javaClassprime) throws BadLocationException, IOException;
	
	public void getGitStatus() throws BadLocationException, IOException, GitAPIException;
	
	public void gitCommit() throws BadLocationException, IOException, GitAPIException;
	
	public void gitPush() throws BadLocationException, IOException, GitAPIException, URISyntaxException;
	
	public void gitPull() throws BadLocationException, IOException, GitAPIException;
	
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

	public void generateCode(String string) throws BadLocationException, IOException ;

	void convertCode(String javaClassprime) throws BadLocationException, IOException;

}
