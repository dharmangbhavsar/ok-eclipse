package edu.ncstate.csc510.okeclipse.handlers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.sohelper.datatypes.StackoverflowAnswer;

import edu.ncstate.csc510.okeclipse.builder.SOAnswerBuilder;
import edu.ncstate.csc510.okeclipse.common.ISoundProgrammer;
import edu.ncstate.csc510.okeclipse.util.Util;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * This class contains the implementation of the Sound Programmer features
 *
 */
public class SoundProgrammerImpl implements ISoundProgrammer {

	/**
	 * Returns the main method that will be displayed on the console
	 * 
	 * @author charan
	 */
	
	public final static String BASE_PATH = System.getProperty("user.dir") + "\\ok-eclipse\\";
	
	public final static String GIT_URL = "https://github.com/dharmangbhavsar/ok-eclipse";
	
	@Override
	public String generateMainMethod() {

		String returnValue;
		returnValue = "public static void main (String[] args) {\r\n" + "		\r\n" + "}\r\n";
		return returnValue;
	}

	/**
	 * @author M.S.Karthik
	 */
	@Override
	public String generateGetterSetter(String javaSourceCode) {
		String getSetResult = javaSourceCode.substring(0, javaSourceCode.lastIndexOf("}"));
		List<String> getSetResultLines = new ArrayList<String>();

		String JSC_temp = javaSourceCode.replaceAll("public", "").replaceAll("private", "").replaceAll("\r\n", "");
		JSC_temp = JSC_temp.substring(JSC_temp.indexOf("{") + 1, JSC_temp.length() - 1);
		int char_flag = 0;
		String JSC_modified = "";
		for (int i = 1; i < JSC_temp.length(); i++) {
			if (JSC_temp.charAt(i) != ' ') {
				JSC_modified += JSC_temp.charAt(i);
				char_flag = 1;
			} else if (char_flag == 1 && JSC_temp.charAt(i) == ' ') {
				JSC_modified += '#';
				char_flag = 0;
			}
		}
		JSC_modified += "#";

		// Removing methods
		while (JSC_modified.contains("{")) {
			int rem1 = JSC_modified.indexOf("{");
			int rem2 = JSC_modified.indexOf("}");
			String rem = JSC_modified.substring(rem1 + 1, rem2);
			rem = "\\{" + rem + "\\}";
			JSC_modified = JSC_modified.replaceFirst(rem, "");
		}

		List<String> JSC_modifiedList = new ArrayList<String>(Arrays.asList(JSC_modified.split("#")));
		JSC_modified = "";
		String item;
		for (int i = 0; i < JSC_modifiedList.size(); i += 2) {
			item = JSC_modifiedList.get(i + 1);
			if (!item.contains("(")) {
				JSC_modified += JSC_modifiedList.get(i) + "#" + item + "#";
			} else {
				i += 2;
			}
		}
		JSC_modified = JSC_modified.replaceAll("\\s+", "").replaceAll(";", "");
		List<String> JSC_memberList = new ArrayList<String>(Arrays.asList(JSC_modified.split("#")));

		for (int i = 0; i < JSC_memberList.size(); i += 2) {
			String member_type = JSC_memberList.get(i);
			String member_identifier = JSC_memberList.get(i + 1);
			String getter = "\r\n" + "public " + member_type + " get" + member_identifier + "()" + "{\r\n" + "\treturn "
					+ member_identifier + ";\r\n" + "}";
			String setter = "\r\n" + "public " + "void" + " set" + member_identifier + "(" + member_type + " "
					+ member_identifier + ")" + "{\r\n" + "\tthis." + member_identifier + " = " + member_identifier
					+ ";\r\n" + "}";
			getSetResultLines.add(getter);
			getSetResultLines.add(setter);
		}

		// Combining the generated methods and given class to supply filled class string
		for (int i = 0; i < getSetResultLines.size(); i++) {
			getSetResult += getSetResultLines.get(i);
		}
		getSetResult += "\r\n}";
		return getSetResult;

	}

	/**
	 * Returns the skeleton of class implementing the interface
	 *
	 * @author charan
	 */
	@Override
	public String implementInterface(String javaSourceCode, String interfaceCode) {

		String returnValue, replaceStr;
		int beginIndex, endIndex;
		String[] method;

		beginIndex = interfaceCode.indexOf("interface");
		beginIndex += 8;
		endIndex = interfaceCode.indexOf("{", beginIndex);
		replaceStr = "implements" + interfaceCode.substring(beginIndex + 1, endIndex) + "{";
		javaSourceCode = javaSourceCode.replaceFirst("\\{", replaceStr);
		beginIndex = interfaceCode.indexOf("{");
		endIndex = interfaceCode.indexOf("}");
		replaceStr = interfaceCode.substring(beginIndex + 1, endIndex);
		returnValue = javaSourceCode.replaceFirst("\\s+\\}", replaceStr + "\\}");
		replaceStr = replaceStr.replaceAll("\\{|\r\n\\s+", "");
		method = replaceStr.split(";");
		for (String s : method) {
			Pattern p = Pattern.compile("(?:public|private|static)\\s(\\w+)\\s(\\w+)\\(\\)");
			Matcher m = p.matcher(s);
			while (m.find()) {
				String dataType = m.group(1);
				String fnctName = m.group(2);
				switch (dataType) {
				case "void":
					replaceStr = "){\r\n\r\n\t}";
					break;
				case "int":
					replaceStr = "){\r\n\r\n\treturn 0;\r\n\t}";
					break;
				case "boolean":
					replaceStr = "){\r\n\r\n\treturn false;\r\n\t}";
					break;
				case "String":
					replaceStr = "){\r\n\r\n\treturn null;\r\n\t}";
					break;
				}
				returnValue = returnValue.replaceFirst("\\);", replaceStr);
			}
		}
		return returnValue;
	}

	// Requirement..

	// input =
	//
	// Collections.sort(list, new Comparator<String>() {
	//
	// @Override
	// public int compare(String o1, String o2) {
	// return 0;
	// }
	//
	// });

	@Override
	public String generateSort(String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertContent(String soCodeContent, int position) throws BadLocationException {
		IDocument doc = Util.getCurrentEditorContent();
		doc.replace(position, 0, soCodeContent);

	}

	@Override
	public void injectCode(String javaClassprime) throws BadLocationException, IOException {

		SOAnswerBuilder answerBuilder = new SOAnswerBuilder();

		char[] characters = javaClassprime.toCharArray();

		int start = -1;
		int end = -1;

		int added = 0;

		for (int index = 0; index < javaClassprime.toCharArray().length; index++) {

			if (end > start) {
				List<StackoverflowAnswer> answers = answerBuilder.extractAnswers(javaClassprime.substring(start, end),
						null);

				String code = getCode(answers);

				code = clean(code);

				int position = added + end + 1;

				if (Util.isNullString(code)) {

					String content = "/** ok_eclipse_fetch : code unavailable **/ \n";
					insertContent(content, position);

					added += content.length();

				} else {

					String content = "/**  ok_eclipse_fetch : " + code + " **/ \n";
					insertContent(content, position);

					added += content.length();
				}

				start = -1;
				end = -1;
			}

			if (start > 0 && end == -1 && characters[index] == '$') {
				end = index + 1;
			} else if (start == -1 && characters[index] == '$') {
				start = index + 1;
			}

		}

	}
	
	@Override
	public void convertCode(String javaClassprime) throws BadLocationException, IOException {
		final String USER_AGENT = "Mozilla/5.0";
		
		String url = "http://167.99.151.232:5005/";
		URL obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "code=" + javaClassprime;
		System.out.println(javaClassprime);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();
		
		insertContent("\n/*\n"+response.toString()+"\n*/\n", javaClassprime.length());
	}
	
	@Override
	public void testCode(String javaClassprime) throws BadLocationException, IOException {
		
		SOAnswerBuilder answerBuilder = new SOAnswerBuilder();

		char[] characters = javaClassprime.toCharArray();

		int start = -1;
		int end = -1;

		int added = 0;

		for (int index = 0; index < javaClassprime.toCharArray().length; index++) {

			if (end > start) {
				String test_cases = answerBuilder.get_test_cases(javaClassprime.substring(start, end));
				
				int position = added + end + 1;

				insertContent("\n\n" + test_cases + "\n\n", position);
				
				return;
			}

			if (start > 0 && end == -1 && characters[index] == '$') {
				end = index + 1;
			} else if (start == -1 && characters[index] == '$') {
				start = index + 1;
			}

		}

	}
	
	@Override
	public void getGitStatus() throws BadLocationException, IOException, GitAPIException {
	    		 try (Git git = Git.open( new File( "C:/Users/tushi/Documents/GitHub/ok-eclipse/.git" )) ){
	                Status status = git.status().call();
	                System.out.println("Added: " + status.getAdded());
	                System.out.println("Changed: " + status.getChanged());
	                System.out.println("Conflicting: " + status.getConflicting());
	                System.out.println("ConflictingStageState: " + status.getConflictingStageState());
	                System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
	                System.out.println("Missing: " + status.getMissing());
	                System.out.println("Modified: " + status.getModified());
	                System.out.println("Removed: " + status.getRemoved());
	                System.out.println("Untracked: " + status.getUntracked());
	                System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
	            }
	}
	
	@Override
	public void gitCommit() throws BadLocationException, IOException, GitAPIException
	{
		final File localPath;
    	FileRepositoryBuilder builder = new FileRepositoryBuilder();

        try (Repository repository = builder
          	    .setGitDir(new File(BASE_PATH + ".git"))
          	    .build()) {
            localPath = repository.getWorkTree();
            
            System.out.println(BASE_PATH);
            
            try (Git git = new Git(repository)) {
                // Stage all files in the repo including new files
            	
            	Iterable<RevCommit> commits = git.log().all().call();
                //int count = 0;
                for (RevCommit commit : commits) {
                    System.out.println("LogCommit: " + commit);
                }
            	
                git.add().addFilepattern(".").call();

                // and then commit the changes.
                git.commit()
                        .setMessage("Commit all changes including additions")
                        .call();

                System.out.println("Committed all changes to repository at " + repository.getDirectory());

            }
        }
	}
	
	@Override
	public void gitPush() throws BadLocationException, IOException, GitAPIException, URISyntaxException
	{
		final File localPath;
    	FileRepositoryBuilder builder = new FileRepositoryBuilder();

        try (Repository repository = builder
          	    .setGitDir(new File(BASE_PATH + ".git"))
          	    .build()) {
            localPath = repository.getWorkTree();

            try (Git git = new Git(repository)) {
            	RemoteAddCommand remoteAddCommand = git.remoteAdd();
            	remoteAddCommand.setName("origin");
            	remoteAddCommand.setUri(new URIish(GIT_URL));
            	remoteAddCommand.call();
                
                //Push Changes
                PushCommand pushCommand = git.push();
                pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("vortexwow911", "vortex911"));
                pushCommand.call();
                
                System.out.println("Pushed all changes to repository");
            }
        }
	}
	
	@Override
	public void gitPull() throws BadLocationException, IOException, GitAPIException
	{
		try {
	        Repository localRepo = new FileRepository(BASE_PATH + ".git");//localPath.getAbsolutePath() + "/.git");
	        Git git = new Git(localRepo);

	            PullCommand pullCmd = git.pull();
	            pullCmd.call();
	            
	        System.out.println("Pulled all changes from repository");
	    } catch (GitAPIException | IOException ex) {
	        //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	    }

	}
	
	private static String clean(String code) {

		String cleanedCode = code;

		if (Util.isNullString(code)) {
			return "";
		} else {

			cleanedCode = cleanedCode.replace("&lt;", "<");
			cleanedCode = cleanedCode.replace("&gt;", ">");
			cleanedCode = cleanedCode.replace("&le;", "<=");
			cleanedCode = cleanedCode.replace("&ge;", ">=");
			cleanedCode = cleanedCode.replace("&amp;", "&");
			cleanedCode = cleanedCode.replace("&quot;", "\"");

			return cleanedCode;
		}

	}

	private String getCode(List<StackoverflowAnswer> answers) {

		String content = "";

		for (StackoverflowAnswer answer : answers) {

			if (answer.isAccepted()) {

				if (answer.getBody().contains("<code>") && answer.getBody().contains("</code>")) {

					try {
						content = answer.getBody().substring(answer.getBody().indexOf("<code>") + 6,
								answer.getBody().indexOf("</code>") - 7);

					} catch (Exception e) {

					}
				}

			}
		}

		return content;
	}

	@Override
	public void generateCode(String javaClassprime) throws BadLocationException, IOException {
		// TODO Auto-generated method stub
		SOAnswerBuilder answerBuilder = new SOAnswerBuilder();

		char[] characters = javaClassprime.toCharArray();

		int start = -1;
		int end = -1;

		int added = 0;
		String code = "";
		for (int index = 0; index < javaClassprime.toCharArray().length; index++) {

			if (end > start) {
				String generateStr = javaClassprime.substring(start, end).trim();
				
				String[] word = generateStr.split(" ");
		        
				int position = added + end + 3;

				switch (word[1]){
		            case "function":
		            {
		            	code = answerBuilder.create_function(word);
		            	insertContent("/*\n" + code + "\n*/\n", position); 
		                break;
		            }
		            case "class":{
		            	code = answerBuilder.create_class(word);
		            	insertContent("/*\n" + code + "\n*/\n", position);
		                break;
		            }
		            case "loop":{
		            	code = answerBuilder.create_loop(word);
		            	insertContent("/*\n" + code + "\n*/\n", position);
		                break;
		            }
		            default:
		            {
		                System.out.println("Error");
		                break;
		            
		            }
		                
		        }

				start = -1;
				end = -1;
			}

			if (start > 0 && end == -1 && characters[index] == '$') {
				end = index - 1;
			} else if (start == -1 && characters[index] == '$') {
				start = index + 1;
			}

		}
	}

	// @Override
	// public void injectCode(String javaClassprime) throws BadLocationException,
	// IOException {
	// String javaClass = javaClassprime;
	// String javaClassset = javaClassprime;
	//
	// int left = javaClass.indexOf("$");// String input = "test string (67)";
	// ArrayList<String> input = new ArrayList<String>();
	// ArrayList<Integer> count = new ArrayList<Integer>();
	// int setpoint = javaClass.indexOf("$");
	// int left_1 = 0;
	// while (left_1 >= 0) {
	// count.add(setpoint);
	// String sub1 = javaClassset.substring(setpoint + 1);
	// int right_pre = sub1.indexOf("$");
	// int right = setpoint + right_pre + 1;
	// String sub = javaClassset.substring(right + 1);
	// left_1 = sub.indexOf("$");
	// if (left_1 >= 0) {
	// setpoint = right + left_1 + 1;
	// count.add(setpoint);
	// }
	//
	// }
	//
	// ArrayList<String> output = new ArrayList<String>();
	// while (left >= 0) {
	// String sub1 = javaClass.substring(left + 1);
	// int right_pre = sub1.indexOf("$");
	// // System.out.println(right_pre);
	// int right = left + right_pre;
	// // System.out.println(right);
	// String sub = javaClass.substring(left + 1, right + 1);
	// // System.out.println(right);
	// // System.out.println(sub1);
	// System.out.println(sub);
	// System.out.println("\n");
	// input.add(sub);
	// // System.out.println("\n");
	// javaClass = javaClass.substring(right + 2);
	// // System.out.println(javaClass);
	// left = javaClass.indexOf("$");
	// }
	// for (String question : input) {
	// System.out.println(question);
	// }
	// // List<StackoverflowAnswer> stackoverflowAnswers = new
	// // List<StackoverflowAnswer>();
	// List<StackoverflowAnswer> myList = new ArrayList<StackoverflowAnswer>();
	// IProgressMonitor monitor = new NullProgressMonitor();
	// SOAnswerBuilder Ansfetcher = new SOAnswerBuilder();
	// for (String question : input) {
	// System.out.println("Entered Loop");
	// System.out.println(question);
	// myList = Ansfetcher.extractAnswers(question, monitor);
	//
	// // System.out.println(myList);
	// // System.out.println("injecting code for "+javaClass);
	// for (StackoverflowAnswer answer : myList) {
	// if (answer.isAccepted()) {
	// String Answer = answer.getBody();
	// // System.out.println(Answer);
	// left = Answer.indexOf("<code>");
	// while (left > 0) {
	// String sub1 = Answer.substring(left + 6);
	// int right_pre = sub1.indexOf("</code>");
	// int right = left + right_pre;
	// // System.out.println(right);
	// String sub = Answer.substring(left + 6, right - 1);
	// output.add(sub);
	// // System.out.println(sub1);
	// System.out.println(sub);
	// // insertContent(sub,0);
	// // System.out.println("\n");
	// Answer = Answer.substring(right - 1);
	// left = Answer.indexOf("<code>");
	// }
	// }
	// }
	// }
	//
	// for (int x = 0; x < output.size(); x++) {
	// insertContent(output.get(x), count.get(x));
	// }
	// }

//
}
// {
// if (answer.isUpVoted())
// {
// String Answer = answer.getBody();
// System.out.println(Answer);
// int left = Answer.indexOf("<code>");
// while (left > 0)
// {
// String sub1 = Answer.substring(left+6);
// int right_pre = sub1.indexOf("</code>");
// int right = left + right_pre;
// //System.out.println(right);
// String sub = Answer.substring(left+6, right-1);
// //System.out.println(sub1);
// System.out.println(sub);
// //System.out.println("\n");
// Answer = Answer.substring(right-1);
// left = Answer.indexOf("<code>");
// }
// }
// }
