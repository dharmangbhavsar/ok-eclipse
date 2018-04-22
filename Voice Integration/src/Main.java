import java.lang.Runtime;
import java.io.*;

public class Main {
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		try {
	        String s = null;
	
			Process p = Runtime.getRuntime().exec("python S2T.py\n cmd/");
			
			BufferedReader stdInput = new BufferedReader(new 
	            InputStreamReader(p.getInputStream()));
	
	       // read the output from the command
	       System.out.println("Here is the standard output of the command:\n");
	       while ((s = stdInput.readLine()) != null) {
	           System.out.println(s);
	       }
	       System.out.println("--");
	       System.exit(0);
	       
		}
		catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}
