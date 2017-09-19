package hw1;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * Homework 1 view class.
 * Provides the GUI.
 * 
 * @author Priyanka Kogta
 * @version 1
 */
public class HW1View extends GridPane {
	private Label labelColor = null;
	
	private Slider sliderRed = null;
	private Slider sliderGreen = null;
	private Slider sliderBlue = null;
	
	private Label labelRedName = null;
	private Label labelGreenName = null;
	private Label labelBlueName = null; 
	
	private Label labelRedValue = null;
	private Label labelGreenValue = null;
	private Label labelBlueValue = null; 


	/**
	 * Creates an instance of <code>HW1View</code> class using default parameters
	 */
	public HW1View() { this(0, 0, 0); }

	/**
	 * Creates an instance of <code>HW1View</code> class.
	 *
	 * @param r The initial red component value.
	 */
	public HW1View(double r, double g, double b) {
		super();

		setPadding(new Insets(10, 10, 10, 10));
		setVgap(10);
		setHgap(10);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(25);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(50);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(25);
		getColumnConstraints().addAll(column1, column2, column3);
		
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(70);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(10);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(10);
		RowConstraints row4 = new RowConstraints();
		row3.setPercentHeight(10);
		getRowConstraints().addAll(row1, row2, row3, row4);

		sliderRed = new Slider(0, 100, r);
		sliderRed.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		sliderRed.setId("sliderRed");
		add(sliderRed, 1, 1);
		
		sliderGreen = new Slider(0, 100, g);
		sliderGreen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		sliderGreen.setId("sliderGreen");
		add(sliderGreen, 1, 2);
		
		sliderBlue = new Slider(0, 100, b);
		sliderBlue.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		sliderBlue.setId("sliderBlue");
		add(sliderBlue, 1, 3);
		
		labelRedValue = new Label(); // shows the number
		labelRedValue.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelRedValue.setId("labelRedValue");
		add(labelRedValue, 2, 1);
		
		labelGreenValue = new Label();
		labelGreenValue.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelGreenValue.setId("labelGreenValue");
		add(labelGreenValue, 2, 2);
		
		labelBlueValue = new Label();
		labelBlueValue.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelBlueValue.setId("labelBlueValue");
		add(labelBlueValue, 2, 3);

		labelRedName = new Label("Red"); // shows the label
		labelRedName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelRedName.setId("labelRedName");
		add(labelRedName, 0, 1);
		
		labelGreenName = new Label("Green");
		labelGreenName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelGreenName.setId("labelGreenName");
		add(labelGreenName, 0, 2);
		
		labelBlueName = new Label("Blue");
		labelBlueName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		labelBlueName.setId("labelBlueName");
		add(labelBlueName, 0, 3);
		
		labelColor = new Label(); // screen
		labelColor.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		add(labelColor, 0, 0, 3, 1);
		labelColor.setId("labelColor");
	}

	/**
	 * Sets the new color in the view.
	 *
	 * @param c The new color.
	 */
	public void setColor(Color c, double r, double g, double b) {
		labelColor.setStyle("-fx-background-color: rgb(" + c.getRed() * 100 + "%, " + c.getGreen() * 100 + "%, " + c.getBlue() * 100 + "%);");
		labelRedValue.setText(String.format("%.2f", r));
		labelGreenValue.setText(String.format("%.2f", g));
		labelBlueValue.setText(String.format("%.2f", b));
	}

	/**
	 * Gets the red component.
	 * 
	 * @return The red component.
	 */
	public final double getRed() { return redProperty().get(); }
	
	/**
	 * Gets the green component.
	 * 
	 * @return The green component.
	 */
	public final double getGreen() { return greenProperty().get(); }
	
	public Label getLabelGreenValue() { return labelGreenValue; }

	/**
	 * Gets the blue component.
	 * 
	 * @return The blue component.
	 */
	public final double getBlue() { return blueProperty().get(); }
	
	
	/**
	 * Sets the red component of light.
	 * 
	 * @param value The red component.
	 */
	public final void setRed(double value) { redProperty().set(value); }

	/**
	 * Sets the green component of light.
	 * 
	 * @param value The green component.
	 */
	public final void setGreen(double value) {
		greenProperty().set(value);
	}
	
	/**
	 * Sets the blue component of light.
	 * 
	 * @param value The blue component.
	 */
	public final void setBlue(double value) { blueProperty().set(value); }

	
	/**
	 * Gets the red component.
	 * 
	 * @return The red component property.
	 */
	public DoubleProperty redProperty() { return sliderRed.valueProperty(); }
	
	/**
	 * Gets the green component.
	 * 
	 * @return The green component property.
	 */
	public DoubleProperty greenProperty() { return sliderGreen.valueProperty(); }
	
	/**
	 * Gets the blue component.
	 * 
	 * @return The blue component property.
	 */
	public DoubleProperty blueProperty() { return sliderBlue.valueProperty(); }
	
}
