package hw2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Homework 2 main class.
 *
 * @author Priyanka Kogta
 * @version 1
 */
public class HW2 extends Application {

    /**
     * Creates GUI and shows the application window.
     *
     * @param stage Top level container.
     */
    @SuppressWarnings("unused")
	public void start(Stage stage) {
        HW2Model model = new HW2Model();
        HW2View view = new HW2View();
        Scene scene = new Scene(view, 800, 600);

        try {
            scene.getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
            stage.getIcons().add(new Image(getClass().getResource("hw2toptitle.png").toExternalForm()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("HW2: prikogta");
        stage.setScene(scene);
        HW2Controller controller = new HW2Controller(model, view, stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) { Platform.exit(); }
        });
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
