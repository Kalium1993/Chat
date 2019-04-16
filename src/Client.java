import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	public static void main(String[] args) {
		try {
			Socket client = new Socket("127.0.0.1",12345);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter output = new PrintWriter(client.getOutputStream());
			
			output.println(input);
			
			client.close();
			input.close();
			output.close();
		} 
		
		catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
