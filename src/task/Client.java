package task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * This class is responsible for client side of chatting application.
 * it has two input fields, one for name and second for message.
 * @author Shakhzoda Ismatullaeva
 *
 */
public class Client extends Application {
	
	ClientSideHandler handler;
	DataOutputStream outputStream = null;
	public static TextArea displayAllText;
	ScrollPane scrollpane;
	TextField name;
	TextField message;
	Button send;
	VBox v;
	HBox h;
	
    public static void main(String[] args) {
      launch(args);
    }
/**
 * this function handles the ui part of the application
 * then it creates a socket, afterwards it creates an intance of ClientSideHandler
 * then creates a thread passing the instance of ClientSideHandler. it is possible because ClientSideHandler extends runnable.
 * afterwards it starts the thread.
 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		scrollpane = new ScrollPane();
		displayAllText = new TextArea();
		name = new TextField();
		message = new TextField();
		v = new VBox();
		h = new HBox();
		
		displayAllText.setEditable(false);
		scrollpane.setContent(displayAllText);
		scrollpane.setFitToHeight(true);
		scrollpane.setFitToWidth(true);
		
		name.setPromptText("Enter Display Name");
		message.setPromptText("Enter Your Messages");
		
		send = new Button("Send Message");
		send.setOnAction(new sendButtonListener());
		
		h.getChildren().addAll(name,message,send);
		v.getChildren().addAll(scrollpane,h);
		v.setVgrow(scrollpane, Priority.ALWAYS);
		
		Scene scene = new Scene(v, 600,600);
		primaryStage.setTitle("Client Window");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		 try { 
			 Socket socket = new Socket("localhost", 4000); 
			 outputStream = new DataOutputStream(socket.getOutputStream());
			 handler = new ClientSideHandler(socket);
			 Thread client = new Thread(handler);
			 client.start();       
	        } catch (IOException e) {
	            System.out.println("Client Error: " + e.getMessage());
	        }
		
	}
	/**
	 * This class implements handler for send button.
	 * if the exit condition is met it disables the buttons and input fields
	 * it calls the function from ClientSideHandler to send message passing name and message contents extracted from the input
	 * @author Shakhzoda Ismatullaeva
	 *
	 */
	private class sendButtonListener implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event)
		{
			String displayName = name.getText();
			String text = message.getText();
			if(text.equals("exit"))
			{
				name.setVisible(false);
				message.setVisible(false);
				send.setVisible(false);
				send.setDisable(true);
			}
			handler.sendMessages(displayName,text);
			message.clear();
		}
	}
}
