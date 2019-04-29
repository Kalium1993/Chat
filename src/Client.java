
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

	private static Socket clientSocket = null;
	private static ObjectOutputStream os = null;
	private static ObjectInputStream is = null;
	private static BufferedReader inputLine = null;
	private static boolean closed = false;

	public static void main(String[] args) {
		int port = 12345;
		String host = "localhost";

		if (args.length < 2) {
			System.out.println("Connecting with server " + host + " in port " + port + "...");
		} else {
			host = args[0];
			port = Integer.valueOf(args[1]).intValue();
			System.out.println("Server: " + host + ", Port: " + port);
		}

		try {
			clientSocket = new Socket(host, port);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			os = new ObjectOutputStream(clientSocket.getOutputStream());
			is = new ObjectInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Unknown " + host);
		} catch (IOException e) {
			System.err.println("No Server found. Please ensure that the Server program is running and try again.");
		}

		if (clientSocket != null && os != null && is != null) {
			try {
				new Thread(new Client()).start();
				while (!closed) {

					String msg = (String) inputLine.readLine().trim();

					os.writeObject(msg);
					os.flush();

				}

				os.close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}

		}
	}

	public void run() {

		String inputLine;

		try {

			while ((inputLine = (String) is.readObject()) != null) {
				System.out.println(inputLine);

				if (inputLine.indexOf("You left the server") != -1)

					break;
			}

			closed = true;
			System.exit(0);

		} catch (IOException | ClassNotFoundException e) {

			System.err.println("Server Process Stopped Unexpectedly!!");

		}
	}
}
