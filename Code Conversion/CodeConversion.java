import java.io.*;
public class CodeConversion  
{  

   public static void main( String gargs[] )  
   {  
      String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec("j2py dharmang.java dharmang.py");
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
	e.printStackTrace();
	}
   }  
} 
