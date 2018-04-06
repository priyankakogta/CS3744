package hw2;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Homework 2 view class.
 * Provides the GUI.
 *
 * @author Priyanka Kogta
 * @version 1
 */
public class HW2View extends VBox {
    private ObjectProperty<File> file = null;
    private ObjectProperty<Point2D> point = null;
    private VBox content = null;
    private Canvas canvas = null;
    private GraphicsContext gc = null;
    private Image lightImage = null;
    private Image backgroundImage = null;
    private ImageCursor lightCursor = null;
    private ImageCursor backgroundCursor = null;
    private ColorPicker lightColorPicker = null;
    private ColorPicker backgroundColorPicker = null;
    private RadioButton lightButton = null;

    /**
     * Creates an instance of <code>HW3View</code> class.
     */
    public HW2View() {
        MenuBar menuBar = new MenuBar();
        HW2View view = this;
        file = new SimpleObjectProperty<File>();
        point = new SimpleObjectProperty<Point2D>();

        lightImage = new Image(getClass().getResource("hw2title.png").toExternalForm());
        lightCursor = new ImageCursor(lightImage, 5, 30);
        backgroundImage = new Image(getClass().getResource("hw2background.png").toExternalForm());
        backgroundCursor = new ImageCursor(backgroundImage, 5, 30);
        Menu menuFile = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem closeItem = new MenuItem("Close");
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openItem.setOnAction(new EventHandler<ActionEvent>() { // shows the file chooser
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
                if (selectedFile != null) {
                    file.set(selectedFile);
                    openItem.setDisable(true);
                    closeItem.setDisable(false);
                }
            }
        });
        closeItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        closeItem.setDisable(true);
        closeItem.setOnAction(new EventHandler<ActionEvent>() { // sets the text focus to the text field.
            @Override
            public void handle(ActionEvent event) {
                file.set(null);
                openItem.setDisable(false);
                closeItem.setDisable(true);
            }
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exitItem.setOnAction(new EventHandler<ActionEvent>() { // Gracefully closes the application when the window is closed.
            @Override
            public void handle(ActionEvent event) { Platform.exit(); }
        });
        menuFile.getItems().addAll(openItem, closeItem, new SeparatorMenuItem(), exitItem);

        Menu menuHelp = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() { // Shows the information dialog.
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Homework 2 solution.");
                alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
                alert.setHeaderText("About Homework 2");
                alert.setTitle("About");
                alert.show();
            }
        });
        aboutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuHelp.getItems().add(aboutItem);

        menuBar.getMenus().addAll(menuFile, menuHelp);

        lightButton = new RadioButton("Light");
        lightButton.setGraphic(new ImageView(lightImage));
        lightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.setCursor(lightCursor);
            }
        });
        lightColorPicker = new ColorPicker();
        RadioButton backgroundButton = new RadioButton("Background");
        backgroundButton.setGraphic(new ImageView(backgroundImage));
        backgroundButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { canvas.setCursor(backgroundCursor); }
        });
        backgroundColorPicker = new ColorPicker();
        ToggleGroup group = new ToggleGroup();
        lightButton.setToggleGroup(group);
        backgroundButton.setToggleGroup(group);
        ToolBar toolbar = new ToolBar(lightButton, lightColorPicker, new Separator(), backgroundButton, backgroundColorPicker);

        canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty().add(menuBar.heightProperty())));
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { point.setValue(new Point2D(e.getX(), e.getY())); }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { point.setValue(new Point2D(e.getX(), e.getY())); }
        });
        gc = canvas.getGraphicsContext2D();
        content = new VBox();
        content.getChildren().addAll(toolbar, canvas);
        this.getChildren().addAll(menuBar, content);
        lightButton.fire();
    }

    /**
     * Draw the background and the grid.
     *
     * @param r The number of rows in the grid.
     * @param c The number of columns in the grid.
     * @param col The background color of the grid.
     * */
    public void drawBackground(int r, int c, Color col) {
        gc.setFill(col);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (int i = 1; i < r; i++) {
            gc.strokeLine(0, i * canvas.getHeight() / r,  canvas.getWidth() - 1 , i * canvas.getHeight() / r);
        }
        for (int i = 1; i < c; i++) {
            gc.strokeLine(i * canvas.getWidth() / c,  0,i * canvas.getWidth() / c, canvas.getHeight() - 1);
        }
    }

    /**
     * Draw a light.
     *
     * @param x The row index of the node.
     * @param y The column index of the node.
     * @param r The number of rows in the grid.
     * @param c The number of columns in the grid.
     * @param col The background color of the grid.
     * */
    public void drawLight(int x, int y, int r, int c, Color col) {
        gc.setFill(col);
        gc.fillRect(y * canvas.getWidth() / c, x * canvas.getHeight() / r, canvas.getWidth() / c, canvas.getHeight() / r);
    }

    /**
     * Gets the file property.
     *
     * @return The file property.
     */
    public ObjectProperty<File> fileProperty() { return file; }

    /**
     * Sets the background color.
     *
     * @param c The background color.
     */
    public void setBackgroundColor(Color c) { backgroundColorPicker.setValue(c); }

    /**
     * Gets the background color property.
     *
     * @return The point property.
     */
    public ObjectProperty<Color> backgroundColorProperty() { return backgroundColorPicker.valueProperty(); }

    /**
     * Gets the cursor point property.
     *
     * @return The point property.
     */
    public ObjectProperty<Point2D> pointProperty() { return point; }

    /**
     * Gets the operation mode.
     * If <code>true</code> then the light editing mode.
     * If <code>false</code> then the background editing mode.
     *
     * @return The operation mode.
     */
    public boolean isLightSelected() { return lightButton.isSelected(); }

    /**
     * Gets the light color picker value.
     *
     * @return The light color picker value.
     */
    public Color getLightColor() { return lightColorPicker.getValue(); }

    /**
     * Gets the canvas width.
     *
     * @return The canvas width.
     */
    public double getCanvasWidth() { return canvasWidthProperty().get(); }

    /**
     * Gets the canvas width property.
     *
     * @return The canvas width property.
     */
    public DoubleProperty canvasWidthProperty() { return canvas.widthProperty(); }

    /**
     * Gets the canvas height.
     *
     * @return The canvas height.
     */
    public double getCanvasHeight() { return canvasHeightProperty().get(); }

    /**
     * Gets the canvas height property.
     *
     * @return The canvas height property.
     */
    public DoubleProperty canvasHeightProperty() { return canvas.heightProperty(); }

}
