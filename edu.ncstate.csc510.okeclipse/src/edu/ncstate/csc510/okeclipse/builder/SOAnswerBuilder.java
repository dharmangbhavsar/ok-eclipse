package edu.ncstate.csc510.okeclipse.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.InsertContentAction;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import com.sohelper.datatypes.GoogleResult;
import com.sohelper.datatypes.StackoverflowAnswer;
import com.sohelper.datatypes.StackoverflowPost;
import com.sohelper.fetchers.GoogleFetcher;
import com.sohelper.fetchers.StackoverflowFetcher;
import com.sohelper.ui.QuestionPage;

import edu.ncstate.csc510.okeclipse.util.Util;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import static java.lang.Integer.parseInt;
import java.util.*;

/**
 * 
 * @author Shrikanth N C
 * To handle and generate stackoverflow report
 *
 */
public class SOAnswerBuilder {

	private static final String FILENAME = "soresponse.html";

	private StringBuffer content = new StringBuffer();
	
	private static final String BASE_PATH = System.getProperty("user.dir") + "\\ok-eclipse\\edu.ncstate.csc510.okeclipse\\src\\edu\\ncstate\\csc510\\okeclipse";
	/**
	 * @author M.S.Karthik
	 * @param questions
	 * @throws IOException
	 * @throws PartInitException
	 */
	// function to get the page from stackoverflow and display it in a readable form
	public void build(List<String> questions) throws IOException, PartInitException {

		IProgressMonitor monitor = new NullProgressMonitor();

		content.append("<html>");
		content.append("<style>");
		content.append("* {\r\n");
		content.append("  margin: 0;\r\n");
		content.append("  padding: 0;\r\n");
		content.append("  font-family: Lato;\r\n");
		content.append("}\r\n");
		content.append("body {\r\n");
		content.append("  padding: 0px;\r\n");
		content.append("  background: #e5e2e6;\r\n");
		content.append("}\r\n");
		content.append(".flatTable {\r\n");
		content.append("  width: 100%;\r\n");
		content.append("  min-width: 500px;\r\n");
		content.append("  border-collapse: collapse;\r\n");
		content.append("  font-weight: bold;\r\n");
		content.append("  color: #6b6b6b;\r\n");
		content.append("}\r\n");
		content.append(".flatTable tr {\r\n");
		content.append("  height: 50px;\r\n");
		content.append("  background: #e0ecf8;\r\n");
		content.append("  border-bottom: rgba(0, 0, 0, 0.05) 1px solid;\r\n");
		content.append("}\r\n");
		content.append(".flatTable td {\r\n");
		content.append("  box-sizing: border-box;\r\n");
		content.append("  padding-left: 30px;\r\n");
		content.append("}\r\n");
		content.append(".flatTable .titleTr {\r\n");
		content.append("  height: 70px;\r\n");
		content.append("  color: #f6f3f7;\r\n");
		content.append("  background: #418a95;\r\n");
		content.append("  border: 0px solid;\r\n");
		content.append("}\r\n");
		content.append(".flatTable .headingTr {\r\n");
		content.append("  height: 30px;\r\n");
		content.append("  background: #63acb7;\r\n");
		content.append("  color: #f6f3f7;\r\n");
		content.append("  font-size: 8pt;\r\n");
		content.append("  border: 0px solid;\r\n");
		content.append("}");
		content.append("</style></head><body>");
		content.append("<h2>");
		content.append("Ok Eclipse Recommendations");
		content.append("</h2>");
		for (String question : questions) {

			content.append("<table class=\"flatTable\">");
			content.append("<tr class=\"titleTr\">");
			content.append("<td class=\"titleTd\">");
			content.append(question);
			content.append("</td>");
			content.append("<td colspan=\"4\">");
			content.append("</td>");
			content.append("<tr class=\"headingTr\">");
			content.append("<td>ACCEPTED</td>");
			content.append("<td>UPVOTES</td>");
			content.append("<td>");
			content.append("SOLUTION");
			content.append("</td>");
			content.append("</tr>");

			buildHTMLBodyContent(extractAnswers(question, monitor));

			content.append("</table>");
			content.append("<br></br>");

			String utubeUrl = "https://www.youtube.com/results?search_query=" + question;
			content.append("<a target=\"_blank\" href=\"" + utubeUrl
					+ "\"><img src=\"https://upload.wikimedia.org/wikipedia/commons/2/2e/YoutubeLogoLink.png\" alt=\"Smiley face\"></a>");

		}

		content.append("</body></html>");

		write();

		openBrowser();

	}

	public List<StackoverflowAnswer> extractAnswers(String question, IProgressMonitor monitor) {
		List<StackoverflowAnswer> stackoverflowAnswers = new LinkedList<>();

		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(activeShell);
		try {
			dialog.run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Fetching recommendations from Q&A repository...", 3);

					List<GoogleResult> googleResults = GoogleFetcher.getGoogleResults(question, monitor);
					List<StackoverflowPost> stackoverflowPosts;
					try {
						stackoverflowPosts = StackoverflowFetcher.getStackoverflowPosts(googleResults, monitor);
						monitor.worked(1);
						CustomQuestionPage questionPage = new CustomQuestionPage();
						stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
								monitor, questionPage));

						if (stackoverflowAnswers.isEmpty()) {
							questionPage.setUpvoted(false);
							stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
									monitor, questionPage));
						}

						if (stackoverflowAnswers.isEmpty()) {
							questionPage.setAccepted(false);
							stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
									monitor, questionPage));
						}

						monitor.worked(2);
						monitor.done();

					} catch (IOException e) {
						e.printStackTrace();
						Util.showError(e, "Unable to get Q&A posts " + e.getMessage());
					}

				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			Util.showError(e, "Unable to fetch from Q&A repository " + e.getMessage());
		}

		return stackoverflowAnswers;
	}
	
	public String get_test_cases(String s) {
		
		String[] tmp= s.split("\\(")[0].split(" ");
		String function_name = tmp[tmp.length - 1];
		String return_type = tmp[tmp.length - 2];

		String var, str;
		String test_cases = "public void unitTesting() { \n try {\n";
		
		s = s.split("\\(")[1].split("\\)")[0];
		String[] param_arr = s.split(",");
		
		if(param_arr.length == 0)
			return function_name + "();";
		
		boolean is_void;
		if(return_type.equals("void"))
			is_void = true;
		else {
			is_void = false;
			if((return_type.indexOf("[") > 0) && (return_type.indexOf("]") > 0)) {
				test_cases += return_type + "[] x;\n";
			} else
				test_cases += return_type + " x;\n";
			
		}
		
		boolean[] bool_arr = { true, false };
		byte[] byte_arr = { Byte.MIN_VALUE, 0, 1, 2, 4, 5, Byte.MAX_VALUE };
		short[] short_arr = { Short.MIN_VALUE, -5, -1, 0, 1, 5, 10, Short.MAX_VALUE };
		int[] int_arr = { Integer.MIN_VALUE, -5, -1, 0, 1, 5, 10, Integer.MAX_VALUE };
		long[] long_arr = { Long.MIN_VALUE, -5, -1, 0, 1, 5, 10, Long.MAX_VALUE };
		float[] float_arr = { Float.MIN_VALUE, -(float) Math.PI, 0, 1, (float) Math.PI, Float.MAX_VALUE };
		double[] double_arr = { Double.MIN_VALUE, -Math.PI , 0, 1, Math.PI, Double.MAX_VALUE };
		char[] char_arr = { 'a', 'z', '0', '9', ',', '\n', '\r', '?', '@', '#', '$' };
		String[] string_arr = { "Apples", "John", "aba", "abCba", "XYZ", "abcdefghijklmnopqrstuvwxyz", "0123456789", "a", "z", "0", "9", "", " ", "\n", "!@#$%^&*(){}: " };
		
		
		// Number of test cases
		int cnt = 5;
		int array_len = 3;
		boolean is_array;
		String arr_str, param;
		while (cnt > 0){
			if(is_void)
				str = function_name + "(";
			else
				str = "x = " + function_name + "(";
			if (param_arr.length > 0){
				for (int i =0;i<param_arr.length;i++){//String param : param_arr) {
					param = param_arr[i];
					arr_str = "";
					is_array = false;
					if ((param.indexOf("[") > 0) && (param.indexOf("]") > 0)){
						is_array = true;
					}
					param = param.replaceAll("\\[", "").replaceAll("\\]", "");
					
					var = param.trim().split(" ")[0];
					
					if (var.equals("boolean")) {
						if(is_array){
							arr_str = "new boolean[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += bool_arr[new Random().nextInt(bool_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += bool_arr[new Random().nextInt(bool_arr.length)] + ",";
					}else if (var.equals("byte")) {
						if(is_array){
							arr_str = "new byte[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += byte_arr[new Random().nextInt(byte_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += byte_arr[new Random().nextInt(byte_arr.length)] + ",";
					}else if (var.equals("short")) {
						if(is_array){
							arr_str = "new short[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += short_arr[new Random().nextInt(short_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += short_arr[new Random().nextInt(short_arr.length)] + ",";
					}else if (var.equals("int")) {
						if(is_array){
							arr_str = "new int[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += int_arr[new Random().nextInt(int_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += int_arr[new Random().nextInt(int_arr.length)] + ",";
					}else if (var.equals("long")) {
						if(is_array){
							arr_str = "new long[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += long_arr[new Random().nextInt(long_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += long_arr[new Random().nextInt(long_arr.length)] + ",";
					}else if (var.equals("float")) {
						if(is_array){
							arr_str = "new float[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += float_arr[new Random().nextInt(float_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += float_arr[new Random().nextInt(float_arr.length)] + ",";
					}else if (var.equals("double")) {
						if(is_array){
							arr_str = "new int[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += double_arr[new Random().nextInt(double_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += double_arr[new Random().nextInt(double_arr.length)] + ",";
					}else if (var.equals("char")) {
						if(is_array){
							arr_str = "new char[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += char_arr[new Random().nextInt(char_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += char_arr[new Random().nextInt(char_arr.length)] + ",";
					}else if (var.equals("String")) {
						if(is_array){
							arr_str = "new String[]{";
							for(int q = 1; q <= array_len; q++){
								arr_str += string_arr[new Random().nextInt(string_arr.length)] + ",";
							}
							arr_str = arr_str.substring(0, arr_str.length() - 1);
							arr_str += "},";
							str += arr_str;
						}else
							str += string_arr[new Random().nextInt(string_arr.length)] + ",";
					}else {
						cnt = 0;
						test_cases = "// Unrecognized variable type: " + var;
						break;
					}					
				}
				str = str.substring(0, str.length() - 1);
			}
			str += ");\n";
			if(!is_void)
				str += "System.out.println(\""+ str.substring(4, str.length() - 2) +" : \" + x);\n";
			test_cases += str;
			cnt--;
		}
		test_cases += "} catch(Throwable e){ e.printStackTrace(); }\n }";
		System.out.println(test_cases);
		return test_cases;
	}
	
	public String create_function(String[] word) throws FileNotFoundException, IOException {
	        String name=word[2];            //name of function
	        String count_parameter= word[5];
	        int count= parseInt(count_parameter);int i=0;
	        String parameter_datatype= word[6];
	        String return_type=word[word.length-1];
	        BufferedReader filein =new BufferedReader(new FileReader(BASE_PATH + "\\skeleton\\function.txt"));
	        
	        String line=filein.readLine();
	        String parameter_final="";
	        System.out.println(System.getProperty("user.dir"));
	        
	        while(i < count){
	       
	           
	            int ascii = 97+i;
	                
	                
	            parameter_final=parameter_final+" "+parameter_datatype+" "+(char)ascii+",";
	            //System.out.println((char)ascii);
	            i++;
	        }
	        if(count!=0){
	            
	            parameter_final=parameter_final.substring(0, parameter_final.length()-1);
	        }
	        
	         if(count==0)
	        {
	            parameter_final="";
	        }
	        
	        String code = "";
	        //System.out.println(parameter_final);
	        while(line!=null)
	        {
	          line = line.replace("datatype", return_type);  
	          line=line.replace("name", name);
	            
	          line=line.replace("parameter", parameter_final);
	          
	          if (return_type.compareTo("void")==0)
	          {
	              line=line.replace("return null;", " ");
	          }
	           if (return_type.compareTo("int")==0 ||return_type.compareTo("float")==0||return_type.compareTo("double")==0)
	          {
	              line=line.replace("return null;", "return 0;");
	          }
	         
	            if (return_type.compareTo("boolean")==0)
	          {
	              line=line.replace("return null;", "return false;");
	          }
	         
	           code += line + "\n";
	            
	           line=filein.readLine();
	        }
	        return code;
	       
	}
	
	public String create_class(String[] word) throws IOException {
	         String name=word[2];            //name of function
	        String count_var= word[4];
	        int count= parseInt(count_var);int i=0;
	        String var_type= word[5];
	        String var_datatype= word[6];
	        BufferedReader filein =new BufferedReader(new FileReader(BASE_PATH + "\\skeleton\\class.txt"));
	        String line=filein.readLine();
	    
	        
	        //generate class abc with 3 public int variables 
	        String final_var="";
	         while(i < count){
	          int ascii = 97+i;
	            final_var=final_var+" "+(char)ascii+",";
	            i++;
	        }
	        if(count!=0){
	            
	            final_var=final_var.substring(0, final_var.length()-1);
	        }
	        
	         if(count==0)
	        {
	          final_var  ="";
	        }
	         final_var=final_var+";";
	         name=name.toUpperCase();
	         String code = "";
	         while(line!=null)
	        {
	           
	         line=line.replace("class_name", name);
	         line=line.replace("variable_type", var_type);
	         line=line.replace("variable_data_type", var_datatype);
	         line=line.replace("variable_name",final_var);
	          
	         
	         code += line + "\n";
	            
	           line=filein.readLine();
	        }
	         return code;
	        
	}
	public String create_loop(String[] word) throws IOException {
	      String limit=word[3];            //number of iterations
	        int count= parseInt(limit);int i=0;
	        BufferedReader filein =new BufferedReader(new FileReader(BASE_PATH + "\\skeleton\\loop.txt"));
	        
	        String line=filein.readLine();
	    
	        String code = "";
	        
	        //generate class abc with 3 public int variables 
	         while(line!=null)
	        {
	           
	        	line=line.replace("limit", limit);
	          
	        	code += line + "\n";
	            
	           line=filein.readLine();
	        }
	         return code;
	          
	}

//	private void openExternalBrowser(URL url) throws PartInitException, MalformedURLException {
//		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
//		browser.openURL(url);
//	}

	private void openBrowser() throws PartInitException, MalformedURLException {
		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport()
				.createBrowser("org.eclipse.ui.browser");
		browser.openURL((getResponseFile()).toURI().toURL());
	}

	private void write() throws IOException {

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getResponseFile()), StandardCharsets.UTF_8)))) {

			out.println(content);

		} catch (IOException e) {
			throw e;
		}
	}

	private static File getResponseFile() {
		return new File(Util.getPrefixPath() + FILENAME);
	}

	private void buildHTMLBodyContent(List<StackoverflowAnswer> stackoverflowAnswers) {

		for (StackoverflowAnswer answer : stackoverflowAnswers) {

			content.append("<tr><td>");
			if (answer.isAccepted()) {
				content.append("Yes");
			} else {
				content.append("No");
			}

			content.append("</td>");
			content.append("<td>");
			content.append(answer.getVoteCount());
			content.append("</td>");
			content.append("<td>");
			content.append(answer.getBody());
			content.append("<br /><br />");
			content.append(answer.getUrl());
			content.append("</td></tr>");
		}

	}

	class CustomQuestionPage extends QuestionPage {

		private boolean accepted = true;
		private boolean upvoted = true;

		public void setAccepted(boolean accepted) {
			this.accepted = accepted;
		}

		public void setUpvoted(boolean upvoted) {
			this.upvoted = upvoted;
		}

		@Override
		public boolean isAcceptedOnly() {
			return accepted;
		}

		@Override
		public boolean isUpVotedOnly() {
			return upvoted;
		}
	}

}
