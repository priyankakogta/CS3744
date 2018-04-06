package hw2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

/**
 * Homework 2 controller class.
 * Coordinates model and view.
 *
 * @author Priyanka Kogta
 * @version 1
 */
public class HW2Controller {
    private HW2Model model = null;
    private HW2View view = null;
    private Stage stage = null;
    private File file = null;
    private static final String DELIMITER = ",";

    /**
     * Creates an instance of <code>HW3Controller</code> class.
     *
     * @param m The model object.
     * @param v The view object.
     * @param s The top level container (used to create a dialog).
     */
    public HW2Controller(HW2Model m, HW2View v, Stage s) {
        model = m;
        view = v;
        stage = s;
        model.nameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { stage.setTitle("HW2: prikogta" + (newValue.equals("") ? "" : " - " + newValue)); }
        });
        model.rowCountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });
        model.columnCountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });
        model.backgroundColorProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                view.setBackgroundColor(newValue);
                draw();
            }
        });
        model.getLightMap().addListener(new MapChangeListener<HW2Model.LightKey, Color>() {
            @Override
            public void onChanged(Change<? extends HW2Model.LightKey, ? extends Color> change) { draw(); }
        });
        view.fileProperty().addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                if (newValue != null) {
                    file = newValue;
                    load(file);
                    draw();
                }
                else {
                    save(file);
                    model.reset();
                }
            }
        });
        view.backgroundColorProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) { model.setBackgroundColor(newValue); }
        });
        view.pointProperty().addListener(new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                if (view.isLightSelected()) {
                    model.addLight(newValue.getX(), newValue.getY(), view.getCanvasWidth(), view.getCanvasHeight(), view.getLightColor());
                }
                else {
                    model.removeLight(newValue.getX(), newValue.getY(), view.getCanvasWidth(), view.getCanvasHeight());
                }
            }
        });
        view.canvasHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });
        view.canvasWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });
        model.reset();
    }

    /**
     * Updates the view (draws the canvas).
     *
     */
    public void draw() {
        view.drawBackground(model.getRowCount(), model.getColumnCount(), model.getBackgroundColor());
        for (HW2Model.LightKey key : model.getLightMap().keySet()) {
            view.drawLight(key.getRow(), key.getColumn(), model.getRowCount(), model.getColumnCount(), model.getLightMap().get(key));
        }
    }

    /**
     * Loads the file.
     *
     * @param f The file.
     */
    public void load(File f) {
        BufferedReader br = null;
        String line = null;
        String[] tokens = null;
        try {
            br = new BufferedReader(new FileReader(f));
            line = br.readLine();
            model.setName(line);
            line = br.readLine();
            tokens = line.split(DELIMITER);
            model.setRowCount(Integer.parseInt(tokens[0]));
            model.setColumnCount(Integer.parseInt(tokens[1]));
            line = br.readLine();
            tokens = line.split(DELIMITER);
            model.setBackgroundColor(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
            while ((line = br.readLine()) != null) {
                tokens = line.split(DELIMITER);
                model.addLight(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
            }
            view.setBackgroundColor(model.getBackgroundColor());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes the currently open file.
     * Saves the data and resets the view and the model to the initial state.
     *
     */
    public void close() {
        save(file);
        model.reset();
    }

    /**
     * Saves the currently open file.
     *
     */
    public void save(File f) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write(model.getName());
            bw.write("\n");
            bw.write(model.getRowCount() +"," + model.getColumnCount());
            bw.write("\n");
            bw.write(model.getBackgroundColor().getRed() * 100 + "," +
                    model.getBackgroundColor().getGreen() * 100 + "," +
                    model.getBackgroundColor().getBlue() * 100 + "," +
                    model.getBackgroundColor().getOpacity() * 100);
            bw.write("\n");
            for (HW2Model.LightKey key : model.getLightMap().keySet()) {
                Color c = model.getLightMap().get(key);
                bw.write(key.getRow() + "," + key.getColumn() + "," + c.getRed() * 100 + "," + c.getGreen() * 100 + "," + c.getBlue() * 100 + "," + c.getOpacity() * 100);
                bw.write("\n");
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
