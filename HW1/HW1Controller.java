package hw1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

/**
 * Homework 1 controller class. Coordinates model and view.
 * 
 * @author Priyanka Kogta
 * @version 1
 */
public class HW1Controller {
	private HW1Model model = null;
	private HW1View view = null;

	/**
	 * A class that describes a change listener for the model's color property.
	 */
	private class ModelListener implements ChangeListener<Color> {
		@Override
		public void changed(ObservableValue<? extends Color> o, Color oldVal, Color newVal) {
			view.setColor(newVal, model.getRed(), model.getGreen(), model.getBlue());
		}
	};

	/**
	 * A class that describes a change listener for the view's temperature
	 * property in red.
	 */
	private class RedViewListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
			model.setRed(newVal.doubleValue());
		}
	};

	/**
	 * A class that describes a change listener for the view's temperature
	 * property in green.
	 */
	private class GreenViewListener implements ChangeListener<Number> {
		public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
			model.setGreen(newVal.doubleValue());
			// view.getLabelGreenValue().setText(String.format("%.2f",
			// newVal.doubleValue()));

		}
	}

	/**
	 * A class that describes a change listener for the view's temperature
	 * property in blue.
	 */
	private class BlueViewListener implements ChangeListener<Number> {
		public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
			model.setBlue(newVal.doubleValue());
		}
	}

	/**
	 * Creates an instance of <code>HW1Controller</code> class.
	 *
	 * @param m
	 *            The model object.
	 * @param v
	 *            The view object.
	 */
	public HW1Controller(HW1Model m, HW1View v) {
		this(m, v, 0, 0, 0);
	}

	/**
	 * Creates an instance of <code>HW1Controller</code> class.
	 *
	 * @param m
	 *            The model object.
	 * @param v
	 *            The view object.
	 * @param red
	 *            the red color
	 * @param green
	 *            The green color.
	 * @param blue
	 *            The blue color
	 */
	public HW1Controller(HW1Model m, HW1View v, double red, double green, double blue) {
		model = m;
		view = v;
		ModelListener modelListener = new ModelListener();

		RedViewListener redViewListener = new RedViewListener();
		GreenViewListener greenViewListener = new GreenViewListener();
		BlueViewListener blueViewListener = new BlueViewListener();

		model.colorProperty().addListener(modelListener);
		view.redProperty().addListener(redViewListener);
		view.greenProperty().addListener(greenViewListener);
		view.blueProperty().addListener(blueViewListener);
		view.setRed(red > 0 ? 0 : 100); // given in class. This is to make the screen black.
		view.setRed(red);
		view.setGreen(green);
		view.setBlue(blue);
	}
}
