import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{
	
	private static ServerSocket server;
	private Socket client;
	private BufferedReader input;
	private PrintStream output;
	
	public Server(Socket client) {
		this.client = client;
		try {
			
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintStream(client.getOutputStream());
			
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void run() {
		try {
			String clientMessage = input.readLine();
			
			while (true) {
				output.println(clientMessage);
				clientMessage = input.readLine();
				
				if (clientMessage.equals("/quit")) {
					client.close();
					break;
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		server = new ServerSocket(12345);
		System.out.println("Server initialized in port 12345");
		
		while (true) {
			System.out.println("Server is waiting for connections...");
			
			Socket client = server.accept();
			System.out.println( " has connected");
			
			Thread t = new Thread();
			t.start();
		}
		
	}
	
//	public static void main(String[] args) {
//		try {
//			ServerSocket server = new ServerSocket(12345);
//			System.out.println("Server initialized in port 12345");
//			System.out.println("Server is waiting for connections...");
//
//			Socket client = server.accept();
//
//			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
//			PrintStream output = new PrintStream(client.getOutputStream(), true);
//			
//			//String serverMessage;
//			
//			while (true) {
//				String clientMessage = input.readLine();
//				System.out.println("<" + client.getInetAddress().getHostAddress() + "> " + clientMessage);
//
//				if (clientMessage.equals("/quit")) {
//					System.out.println(client.getInetAddress().getHostAddress() + ": " + "Left the server");
//					input.close();
//					output.close();
//					client.close();
//					server.close();
//					break;
//				}
//
//			}
//
//		}
//
//		catch (IOException ex) {
//			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
}
