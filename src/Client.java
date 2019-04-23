import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

	private static Socket client;
	private BufferedReader input;
	private PrintStream output;
	private String ip;
	private int port;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void connect() throws IOException {
		client = new Socket(ip, port);
		System.out.println("You're connected with the server");

		input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		output = new PrintStream(client.getOutputStream());
	}

	public void sendMessage(String msg) throws IOException {
		output.println(msg);
	}

	public void run() {
		String message;

		while (true) {
			try {
				message = input.readLine();
				
				if (message == null) {
					System.out.println("Connection lost");
					System.exit(0);
				}

			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
	
	public static void main(String[] args) throws IOException {
		
		Client client = new Client("127.0.0.1", 12345);
		
		client.connect();
		client.start();
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		
		String message;
		while (true) {
			message = bf.readLine();
			
			client.sendMessage(message);
			
			if (message.equals("/quit")) {
				break;
			}
		}
	}

//	public static void main(String[] args) {
//		try {
//			Socket client = new Socket("127.0.0.1", 12345);
//
//			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//			PrintStream output = new PrintStream(client.getOutputStream());
//			
//			System.out.println("Write your nickname");
//			String nick = input.readLine();
//			
//			output.println(nick + " has connected with the server");
//			
//			while (true) {
//				
//
//				String message = input.readLine();
//				message = message.trim();
//
//				output.println(message);
//
//				if (message.equals("/quit")) {
//					output.println("Left the server");
//					client.close();
//					input.close();
//					output.close();
//					break;
//				}
//
//				if (message.equals("/nickname")) {
//					//client.set
//				}
//
//			}
//
//		}
//
//		catch (IOException ex) {
//			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
}
