package rumor_simulator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Param_test {
	
	public static void main(String args[]) throws Exception{
		final String USER_AGENT = "Mozilla/5.0";
		
		String url = "http://167.99.151.232:5005/";
		URL obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "code=public class Main_Class {\r\n" + 
				"	\r\n" + 
				"	public static void main(String args[]){\r\n" + 
				"		System.out.println(\"Hello World!\")\r\n" + 
				"	}\r\n" + 
				"}";
		
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
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());
	}
}
