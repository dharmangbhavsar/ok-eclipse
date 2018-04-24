/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
Algorithm
Parse a scentance word by word.
On detection of a keyword check previous or next word for consonant of code
for e.g generate function abc implies function keyword and abc as consonant of the code

example syntax to speak :
generate function abc that has 3 int parameters and returns int
generate function abc that has 1 float parameter and returns float
generate function abc that has 0 int parameters and returns void

generate class abc with 3 public/private/protected variables.  

generate loop for 10 iterations. 


*/
package code_generation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.Scanner;

public class Code_generation {

    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Please Enter the sentence");
        Scanner sc = new Scanner(System.in);
        String s= sc.nextLine();
        String[] word = s.split(" ");
      
     
        switch (word[1]){
            case "function":
            {
                create_function(word);
                break;
            }
            case "class":{
                create_class(word);
                
                break;
            }
            case "loop":{
                create_loop(word);
                
                break;
            }
            default:
            {
                System.out.println("Error");
                break;}
            
            }
                
        }
        
    

    private static void create_function(String[] word) throws FileNotFoundException, IOException {
                String name=word[2];            //name of function
                String count_parameter= word[5];
                int count= parseInt(count_parameter);int i=0;
                String parameter_datatype= word[6];
                String return_type=word[word.length-1];
                BufferedReader filein =new BufferedReader(new FileReader("src\\code_generation\\function.txt"));
                BufferedWriter fileout =new BufferedWriter(new FileWriter("src\\code_generation\\function_output.java"));
                String line=filein.readLine();
                String parameter_final="";
                
                
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
                 
                 
                    System.out.println(line);
                  
                    
                   try{
                    fileout.write(line);
                    fileout.newLine();
                   }catch(IOException e){
                       System.out.println(e);
                   }
                   line=filein.readLine();
                }
                fileout.close();
                
                
               
    }

    private static void create_class(String[] word) throws IOException {
                 String name=word[2];            //name of function
                String count_var= word[4];
                int count= parseInt(count_var);int i=0;
                String var_type= word[5];
                String var_datatype= word[6];
                BufferedReader filein =new BufferedReader(new FileReader("src\\code_generation\\class.txt"));
                BufferedWriter fileout =new BufferedWriter(new FileWriter("src\\code_generation\\class_output.java"));
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
                 while(line!=null)
                {
                   
                 line=line.replace("class_name", name);
                 line=line.replace("variable_type", var_type);
                 line=line.replace("variable_data_type", var_datatype);
                 line=line.replace("variable_name",final_var);
                  
               
                 
                    System.out.println(line);
                  
                    
                   try{
                    fileout.write(line);
                    fileout.newLine();
                   }catch(IOException e){
                       System.out.println(e);
                   }
                   line=filein.readLine();
                }
                fileout.close();
                
    }
    private static void create_loop(String[] word) throws IOException {
              String limit=word[3];            //number of iterations
                int count= parseInt(limit);int i=0;
                BufferedReader filein =new BufferedReader(new FileReader("src\\code_generation\\loop.txt"));
                BufferedWriter fileout =new BufferedWriter(new FileWriter("src\\code_generation\\loop_output.java"));
                String line=filein.readLine();
            
                
                //generate class abc with 3 public int variables 
                 while(line!=null)
                {
                   
                 line=line.replace("limit", limit);
                    System.out.println(line);
                  
                    
                   try{
                    fileout.write(line);
                    fileout.newLine();
                   }catch(IOException e){
                       System.out.println(e);
                   }
                   line=filein.readLine();
                }
                fileout.close();
                  
    }
    
}
