
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import java.net.ServerSocket;

public class Server {

	private static ServerSocket serverSocket = null;

	private static Socket clientSocket = null;

	public static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	public static void main(String args[]) {

		int port = 12345;

		if (args.length < 1) {

			System.out.println("Server initialized in port " + port);
			System.out.println("Server is waiting for connections...");

		} else {
			port = Integer.valueOf(args[0]).intValue();

			System.out.println("Server initialized in port " + port);
			System.out.println("Server is waiting for connections...");
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Server Socket cannot be created");
		}

		int clientNum = 1;
		while (true) {
			try {

				clientSocket = serverSocket.accept();
				ClientThread clientThread = new ClientThread(clientSocket, clients);
				clients.add(clientThread);
				clientThread.start();
				System.out.println("Client " + clientNum + " is connected!");
				clientNum++;

			} catch (IOException e) {

				System.out.println("Client could not be connected");
			}

		}

	}
}
