package hw1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Homework 1 main class.
 * 
 * @author Priyanka Kogta
 * @version 1
 */
public class HW1 extends Application {

	/**
	 * Creates GUI and shows the application window.
	 * 
	 * @param stage Top level container.
	 */
	public void start(Stage stage) {
		HW1Model model = new HW1Model();
		HW1View view = new HW1View();
		Scene scene = new Scene(view, 400, 500);
		try {
			scene.getStylesheets().add(getClass().getResource("hw1.css").toExternalForm());
		} catch(Exception e) {
			e.printStackTrace();
		}
		stage.setTitle("HW1: prikogta");
		stage.setScene(scene);
		@SuppressWarnings("unused")
		HW1Controller controller = new HW1Controller(model, view, 0, 0, 0);
		stage.show();
	}
	
	/**
	 * Invokes the program from the command line.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
