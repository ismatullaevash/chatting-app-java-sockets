package task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
/**
 * This class is responsible for echoing messages sent to all clients that are connected to server
 * If the exit condition is met it disconnects the client and shows information on application
 * @author Shakhzoda Ismatullaeva
 *
 */
public class Echoer extends Thread {

	private Socket socket;
	private ArrayList<Echoer> instances;
	private BufferedReader input;
	private PrintWriter writer;
/**
 * This function accepts arguments and initializes its class members based off them
 * @param socket is of type Socket and used to initialize socket,BufferedReader and PrintWriter
 * @param instances is an ArrayList that hold instances of type Echoer, it is used to initialize class member instances
 * @throws IOException
 */
	public Echoer(Socket socket, ArrayList<Echoer> instances) throws IOException {
		this.socket = socket;
		this.instances = instances;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(), true);
	}
/**
 * This function is responsible for updating ui with newly received messages. if the exit condition is met then it updates ui to reflect that user left.
 */
	@Override
	public void run() {
		Platform.runLater(()-> Server.label.setText(Server.label.getText() + "Obtained client connection from " + socket + '\n'));
		try {
			while (true) {
				String echoString = input.readLine();
				try {
					
					if(!echoString.contains("exit"))
					{
						messageEveryone(echoString);
					}
					else
					{
						Platform.runLater(()-> Server.label.setText(Server.label.getText() + "User logged out!" + '\n'));
						break;
					}
					
				}catch(Exception error)
				{
					System.out.println("User Exited");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Exception has occured while echoing messages " + e.getMessage());
		} finally {
			writer.close();
			try {
				input.close();
				socket.close();
			} catch (IOException e) {	
				System.out.println("Exception while closing socket " + e.getMessage());
			}

		}

	}
/**
 * this function is responsible for sending messages to all of the connected clients
 * It also updates server ui to reflect every messages that clients exchange with
 * @param echoString is of type String that holds the message
 */
	private void messageEveryone(String echoString) {
		
		Platform.runLater(()-> Server.label.setText(Server.label.getText() + echoString + '\n'));
		for (Echoer instance : instances) {
			instance.writer.println(echoString);
		}
		
	}

}
