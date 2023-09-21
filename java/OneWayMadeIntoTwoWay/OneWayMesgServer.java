/*
 * Author: Aaron L. Keys
 * Last Modified: 18 Sept 2023
 * Implementation of a two-way message server in java
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the server's job
 * It receives the connection from client
 * and prints messages sent from the client
 */
public class OneWayMesgServer {
	/*
	 * The server program starts from here
	 */
	public static void main(String args[]) {
		// Server needs the port number to listen on
		if (args.length != 1) {
			System.out.println("usage: java OneWayMesgServer <port>");
			System.exit(1);
		}

		// Get the port on which server should listen */
		int serverPort = Integer.parseInt(args[0]);

		// Be prepared to catch socket related exceptions
		try {
			// Create a server socket with the given port
			ServerSocket serverSock = new ServerSocket(serverPort);
			System.out.println("Waiting for a client ...");

			// Wait to receive a connection request
			Socket clientSock = serverSock.accept();
			System.out.println("Connected to a client at ('" +
					((InetSocketAddress) clientSock.getRemoteSocketAddress()).getAddress().getHostAddress()
					+ "', '" +
					((InetSocketAddress) clientSock.getRemoteSocketAddress()).getPort()
					+ "')");

			// Prepare to read from client
			BufferedReader fromClientReader = new BufferedReader(
					new InputStreamReader(clientSock.getInputStream()));

			// Keep serving the client
			while (true) {
				// Read a message from the client
				String message = fromClientReader.readLine();

				// If we get null, it means client sent EOF
				if (message == null) {
					System.out.println("Client closed connection");
					// clientSock.close();
					break;
				}

				// Display the message
				System.out.println(message);

				// Preparing to write to the server with auto flush on
				PrintWriter toClientWriter = new PrintWriter(clientSock.getOutputStream(), true);
				BufferedReader fromServerKeyboard = new BufferedReader(
						new InputStreamReader(System.in));
				// Keep going until we get end of file (EOF) from the user.

				// Preparing to read from the keyboard on the server
				String ln = fromServerKeyboard.readLine(); // fix this to read from keyboard on server not client

				if (ln == null) {
					System.out.println("Closing connection");
					// clientSock.close();
					break;
				}

				toClientWriter.println("Server: " + ln);

			}
			// close the serversocket, and exit.
			serverSock.close();
			clientSock.close();
		} catch (Exception e) {
			// Print the exception message
			System.out.println(e);
		}
	}
}
