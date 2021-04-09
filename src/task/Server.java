package task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

	/**
	 * This application implements chatting app using sockets
	 * Server utilizes Echoer which is responsible for sending message to all of the connected clients
	 * Class has member that is ArrayList of type Echoer to keep track of all clients
	 * @author Shakhzoda Ismatullaeva
	 * 
	 *
	 */
	public class Server extends Application {
		public static Label label;
		private static ArrayList<Echoer> instances = new ArrayList<Echoer>();
		
		public static void main(String [] args) {
		launch(args);
		}
/**
 * This function creates all the needed visual parts of the application
 * then it creates a thread inside of which service socket is created with port 4000. then Client socket is created and connected to server. 
 * if the connection is successful then console log inform that. then a new instance of echoer is created and added to list. we pass the ArrayList into the constructor.
 * Then function starts thread for echoer, as echoer extends thread. after we start the echoer thread, the outer thread is started too
 */
		@Override
		public void start(Stage primaryStage) throws Exception {
			
			ScrollPane scrollpane = new ScrollPane();
			Scene scene = new Scene(scrollpane, 600, 500);
			label = new Label("Server started at " + new Date() + "\n");
			scrollpane.setContent(label);
			primaryStage.setTitle("Server Window");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Thread newServer = new Thread(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	            	try(ServerSocket serverSocket = new ServerSocket(4000)){
	    				while(true) {
	    						Socket socket = serverSocket.accept(); 
	    						System.out.println("Client Connected");
	    						Echoer echo=new Echoer(socket,instances);
	    						instances.add(echo);
	    						echo.start();		 
	    						} 
	    					}catch(IOException e) {
	    						System.out.println("Exception while running the Server "+e.getMessage());
	    				}}});
			newServer.start();
		}
}
