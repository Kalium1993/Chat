import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

class ClientThread extends Thread {

	private String clientNickname = null;
	private ObjectInputStream is = null;
	private ObjectOutputStream os = null;
	private Socket clientSocket = null;
	private final ArrayList<ClientThread> clients;

	public ClientThread(Socket clientSocket, ArrayList<ClientThread> clients) {

		this.clientSocket = clientSocket;
		this.clients = clients;

	}

	public void run() {

		ArrayList<ClientThread> clients = this.clients;

		try {
			is = new ObjectInputStream(clientSocket.getInputStream());
			os = new ObjectOutputStream(clientSocket.getOutputStream());

			String nickName;
			while (true) {

				synchronized (this) {
					this.os.writeObject("Please, enter your nickname");
					this.os.flush();
					nickName = ((String) this.is.readObject()).trim();

					if ((nickName.indexOf('@') == -1) || (nickName.indexOf('!') == -1)) {
						break;
					} else {
						this.os.writeObject("Username should not contain '@' or '!' characters.");
						this.os.flush();
					}
				}
			}

			System.out.println("Client nickname is " + nickName);

			this.os.writeObject("You're connected with the server, welcome " + nickName 
					+ "\nType '/help' to see the commands");
			this.os.flush();

			synchronized (this) {

				for (ClientThread clientThread : clients) {
					if (clientThread != null && clientThread == this) {
						clientNickname = "@" + nickName;
						break;
					}
				}

				for (ClientThread clientThread : clients) {
					if (clientThread != null && clientThread != this) {
						clientThread.os.writeObject(nickName + " has connected");
						clientThread.os.flush();

					}

				}
			}

			while (true) {
				String line = (String) is.readObject();

				if (line.contains("/")) {
					if (line.startsWith("/quit")) {

						break;
					}
					
					if (line.startsWith("/help")) {
						this.os.writeObject("Type '/users' to see all the online users");
						this.os.flush();
						this.os.writeObject("Type '/nick' to change your nickname");
						this.os.flush();
						this.os.writeObject("Type '@<user> <message>' to send a private message to a user");
						this.os.flush();
						
						continue;
					}

					if (line.startsWith("/users")) {
						this.os.writeObject("Online users:");
						for (ClientThread clientThread : clients) {
							if (clientThread == this) {
								this.os.writeObject("(YOU)" + clientThread.clientNickname.substring(1));
								this.os.flush();
							}

							if (clientThread.clientNickname != this.clientNickname) {
								this.os.writeObject(clientThread.clientNickname.substring(1));
								this.os.flush();
							}

						}
						continue;
					}

					if (line.startsWith("/nick")) {
						String newNick;
						this.os.writeObject("Please, enter your new nickname");
						while (true) {

							synchronized (this) {
								newNick = ((String) this.is.readObject()).trim();

								if ((newNick.indexOf('@') == -1) || (newNick.indexOf('!') == -1)) {
									break;
								} else {
									this.os.writeObject("Username should not contain '@' or '!' characters.");
									this.os.flush();
								}
							}
						}

						synchronized (this) {

							for (ClientThread clientThread : clients) {
								if (clientThread != null && clientThread == this) {
									clientNickname = "@" + newNick;
									clientThread.os.writeObject("You has changed your nickname to " + newNick);
									clientThread.os.flush();
									break;
								}
							}

							for (ClientThread clientThread : clients) {
								if (clientThread != this) {
									clientThread.os.writeObject(nickName + " has changed his/her nickname to " + newNick);
									clientThread.os.flush();
								}
							}
						}

						nickName = newNick;
						continue;

					} else {
						this.os.writeObject("ERROR! Invalid command!\n"
								+ "Please, type '/help' to see all the commands");
						this.os.flush();
						
						continue;
					}
				}

				if (line.startsWith("@")) {
					privateMsg(line, nickName);
				}

				else {
					sendToAll(line, nickName);
				}

			}

			this.os.writeObject("You left the server");
			this.os.flush();
			System.out.println(nickName + " left the server");
			clients.remove(this);

			synchronized (this) {

				if (!clients.isEmpty()) {

					for (ClientThread clientThread : clients) {

						if (clientThread != null && clientThread != this && clientThread.clientNickname != null) {
							clientThread.os.writeObject(nickName + " left the server");
							clientThread.os.flush();
						}
					}
				}
			}

			this.is.close();
			this.os.close();
			clientSocket.close();

		} catch (IOException e) {

			System.out.println("User Session terminated");

		} catch (ClassNotFoundException e) {

			System.out.println("Class Not Found");
		}
	}

	void sendToAll(String line, String nickName) throws IOException, ClassNotFoundException {

		synchronized (this) {

			for (ClientThread clientThread : clients) {

				if (clientThread != null && clientThread.clientNickname != null
						&& clientThread.clientNickname != this.clientNickname) {

					clientThread.os.writeObject("<" + nickName + "> " + line);
					clientThread.os.flush();

				}
			}

		}

	}

	void privateMsg(String line, String nickName) throws IOException, ClassNotFoundException {

		String[] words = line.split(" ", 2);

		if (words.length > 1 && words[1] != null) {

			words[1] = words[1].trim();

			if (!words[1].isEmpty()) {

				for (ClientThread clientThread : clients) {
					if (clientThread != null && clientThread != this && clientThread.clientNickname != null
							&& clientThread.clientNickname.equals(words[0])) {
						clientThread.os.writeObject("*PRIVATE*<" + nickName + "> " + words[1]);
						clientThread.os.flush();

						break;
					}
				}
			}
		}
	}

}