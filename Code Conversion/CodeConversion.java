//import org.python.core.PyInstance;  
//import org.python.util.PythonInterpreter;  
//import org.python.util.*;
import java.io.*;
public class CodeConversion  
{  

   /*PythonInterpreter interpreter = null;  


   public InterpreterExample()  
   {  
      PythonInterpreter.initialize(System.getProperties(),  
                                   System.getProperties(), new String[0]);  

      this.interpreter = new PythonInterpreter();  
   }  

   void execfile( final String fileName )  
   {  
      this.interpreter.execfile(fileName);  
   }  

   PyInstance createClass( final String className, final String opts )  
   {  
      return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");  
   }  
*/
   public static void main( String gargs[] )  
   {  
   /*   InterpreterExample ie = new InterpreterExample();  

      ie.execfile("hello.py");  

      PyInstance hello = ie.createClass("Hello", "None");  

      hello.invoke("run");  */
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
	System.out.println("SOme problem\n");
	e.printStackTrace();
	}
   }  
} 
