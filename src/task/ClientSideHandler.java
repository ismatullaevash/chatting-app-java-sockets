package task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
/**
 * this class handles the client side of the application
 * it ensures that messages are sent out to every single connected client
 * @author Shakhzoda Ismatullaeva
 *
 */
public class ClientSideHandler implements Runnable {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter writer;
	
	public ClientSideHandler(Socket socket) throws IOException
	{
		this.socket = socket;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(), true);
	}
	
	@Override
	public void run() {
		try {
			while(true)
			{
				String response = input.readLine();			
				
				if(response == null || response.contains("exit"))
				{
					Platform.runLater(()-> Client.displayAllText.setText(Client.displayAllText.getText() +  "you left the chat" + '\n'));
					break;
				}
			
				Platform.runLater(()-> Client.displayAllText.setText(Client.displayAllText.getText() +  response + '\n'));
			}
		}catch(IOException e)
		{
			System.out.println("Exception while handling input "+e.getMessage());
		}finally {
			try {
				input.close();
			}catch(IOException e)
			{
				System.out.println("Exception while attempting to close input stream " +e.getMessage());
			}
		}
		
	}
	/**
	 * this function writes the messages into ui 
	 * @param name is of type String that hold the name of the sender
	 * @param text is of type String that hold the contents of the message
	 */
	public void sendMessages(String name, String text)
	{
		writer.println( "["+ name +"]" + " : " + text);
	}

}
