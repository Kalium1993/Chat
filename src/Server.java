import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(12345);
			System.out.println("Server initialized in port 12345...");
			
			Socket client = server.accept();
			System.out.println("Client connected, from IP: " + client.getInetAddress().getHostAddress());
			
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			
			String clientMessage = input.readLine();
			System.out.println(clientMessage);
			
			input.close();
			output.close();
			client.close();
			server.close();
		} 
		
		catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
